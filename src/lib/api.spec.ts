import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest';
import * as authStore from './stores/auth';
import { secureFetch } from './api';
import * as svelteStore from 'svelte/store';

// isTokenExpired는 export되지 않으므로 secureFetch를 통해 간접 테스트
// 별도 export가 필요하다면 api.ts에서 export해도 됨

// JWT payload 생성 유틸
function createJwt(exp: number) {
  const header = btoa(JSON.stringify({ alg: 'HS256', typ: 'JWT' }));
  const payload = btoa(JSON.stringify({ exp }));
  return `${header}.${payload}.signature`;
}

describe('secureFetch', () => {
  let fetchMock: ReturnType<typeof vi.fn>;
  let logoutMock: ReturnType<typeof vi.fn>;

  beforeEach(() => {
    fetchMock = vi.fn(() => Promise.resolve(new Response('ok', { status: 200 })));
    global.fetch = fetchMock as any;

    logoutMock = vi.fn();
    vi.spyOn(authStore, 'logout').mockImplementation(logoutMock);

    // svelte/store의 get을 mock
    vi.spyOn(svelteStore, 'get');
  });

  afterEach(() => {
    vi.restoreAllMocks();
  });

  it('should call fetch without Authorization if no token', async () => {
    vi.spyOn(svelteStore, 'get').mockReturnValue(null);

    await secureFetch('/api/test');
    expect(fetchMock).toHaveBeenCalledWith('/api/test', {});
  });

  it('should call fetch with Authorization if token is valid', async () => {
    // exp: 10분 뒤
    const token = createJwt(Math.floor(Date.now() / 1000) + 600);
    vi.spyOn(svelteStore, 'get').mockReturnValue(token);

    await secureFetch('/api/test', { method: 'POST' });
    expect(fetchMock).toHaveBeenCalledWith(
      '/api/test',
      expect.objectContaining({
        method: 'POST',
        headers: expect.objectContaining({
          Authorization: `Bearer ${token}`
        })
      })
    );
  });

  it('should logout and throw if token is expired', async () => {
    // exp: 1초 전
    const token = createJwt(Math.floor(Date.now() / 1000) - 1);
    vi.spyOn(svelteStore, 'get').mockReturnValue(token);

    await expect(secureFetch('/api/test')).rejects.toThrow('JWT expired');
    expect(logoutMock).toHaveBeenCalled();
    expect(fetchMock).not.toHaveBeenCalled();
  });

  it('should throw and logout if token is invalid', async () => {
    const token = 'invalid.token.value';
    vi.spyOn(svelteStore, 'get').mockReturnValue(token);

    await expect(secureFetch('/api/test')).rejects.toThrow();
    expect(logoutMock).toHaveBeenCalled();
    expect(fetchMock).not.toHaveBeenCalled();
  });

  it('should return fetch result', async () => {
    vi.spyOn(svelteStore, 'get').mockReturnValue(null);

    const res = await secureFetch('/api/test');
    expect(res).toBeInstanceOf(Response);
    const text = await res.text();
    expect(text).toBe('ok');
  });
});
