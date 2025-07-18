import { get } from 'svelte/store';
import { jwtToken, logout } from './stores/auth';

// JWT 토큰을 Authorization 헤더에 자동 첨부하는 fetch wrapper
// 토큰은 sessionStorage에만 저장(보안상 localStorage 사용 금지, XSS 방지)
// 토큰 만료 시 자동 로그아웃

function isTokenExpired(token: string): boolean {
  try {
    const payload = JSON.parse(atob(token.split('.')[1]));
    // exp는 초 단위, Date.now()는 ms 단위
    return payload.exp * 1000 < Date.now();
  } catch {
    return true;
  }
}

export async function secureFetch(input: RequestInfo, init: RequestInit = {}) {
  const token = get(jwtToken);

  if (token) {
    if (isTokenExpired(token)) {
      logout();
      throw new Error('JWT expired. Please log in again.');
    }
    init.headers = {
      ...(init.headers || {}),
      Authorization: `Bearer ${token}`
    };
  }

  return fetch(input, init);
}

// secureFetch 사용 예시 (실제 API 엔드포인트에 맞게 수정 필요)
// import { secureFetch } from '$lib/api';
// async function getProducts() {
//   const res = await secureFetch('/api/products');
//   if (!res.ok) throw new Error('Failed to fetch products');
//   return await res.json();
// }

// 실제 fetch 사용 시 secureFetch로 대체하세요.
