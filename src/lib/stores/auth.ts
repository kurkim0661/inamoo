import { writable } from 'svelte/store';
import type { GoogleUser } from '../types/auth';

// JWT 토큰은 sessionStorage에만 저장(보안상 localStorage 사용 금지, XSS 방지 주석 참고)
const TOKEN_KEY = 'jwt_token';

export const user = writable<GoogleUser | null>(null);
export const jwtToken = writable<string | null>(typeof window !== 'undefined' ? sessionStorage.getItem(TOKEN_KEY) : null);

let googleInitialized = false;

function loadGoogleScript(): Promise<void> {
  return new Promise<void>((resolve, reject) => {
    if (document.getElementById('google-identity')) {
      resolve();
      return;
    }
    const script = document.createElement('script');
    script.src = 'https://accounts.google.com/gsi/client';
    script.async = true;
    script.defer = true;
    script.id = 'google-identity';
    script.onload = () => resolve();
    script.onerror = reject;
    document.head.appendChild(script);
  });
}

export async function initGoogleSSO() {
  await loadGoogleScript();
  if (
    typeof window !== 'undefined' &&
    (window as any).google &&
    (window as any).google.accounts &&
    (window as any).google.accounts.id
  ) {
    (window as any).google.accounts.id.initialize({
      client_id: '694090990244-p82najlcv2n2rhifekg44vnfg30bopa1.apps.googleusercontent.com',
      callback: handleCredentialResponse
    });
    (window as any).google.accounts.id.renderButton(
      document.getElementById('google-signin-btn'),
      { theme: 'outline', size: 'large', width: 240 }
    );
    googleInitialized = true;
  }
}

async function handleCredentialResponse(response: { credential: string }) {
  // JWT 토큰을 sessionStorage에 저장 (XSS 방지: localStorage 사용 금지)
  sessionStorage.setItem(TOKEN_KEY, response.credential);
  jwtToken.set(response.credential);

  // 백엔드에 idToken 전달 및 사용자 정보 등록/조회
  try {
    const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '';
    console.log('VITE_API_BASE_URL:', import.meta.env.VITE_API_BASE_URL);
    const url = `${API_BASE_URL}/api/auth/google`;
    console.log('Requesting URI:', url);
    const res = await fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ idToken: response.credential })
    });

    if (!res.ok) {
      throw new Error('Failed to authenticate with backend');
    }

    const data = await res.json();
    // 백엔드에서 반환한 user 정보로 store 갱신
    user.set(data.user || data);
  } catch (e) {
    // fallback: 프론트에서 디코드한 정보라도 보여줌
    const base64Url = response.credential.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split('')
        .map(function (c) {
          return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        })
        .join('')
    );
    user.set(JSON.parse(jsonPayload));
  }
}

export function logout() {
  user.set(null);
  jwtToken.set(null);
  sessionStorage.removeItem(TOKEN_KEY);
  // Optionally revoke token or sign out from Google
}
