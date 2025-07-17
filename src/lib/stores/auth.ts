import { writable } from 'svelte/store';
import type { GoogleUser } from '../types/auth';

export const user = writable<GoogleUser | null>(null);

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

function handleCredentialResponse(response: { credential: string }) {
  // Decode JWT to get user info
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

export function logout() {
  user.set(null);
  // Optionally revoke token or sign out from Google
}
