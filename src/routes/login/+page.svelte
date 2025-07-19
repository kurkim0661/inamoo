<script lang="ts">
  import { onMount } from 'svelte';
  import { user, initGoogleSSO, logout } from '$lib/stores/auth';
  import { goto } from '$app/navigation';
  import { get } from 'svelte/store';

  let nickname = '';
  let showExtraInfo = false;
  let saving = false;
  let error = '';

  onMount(() => {
    initGoogleSSO();
  });

  $: if ($user) {
    // nickname이 없으면 추가 정보 입력 폼 노출
    if (!$user.nickname) {
      showExtraInfo = true;
    } else {
      goto('/');
    }
  }
</script>

<style>
.login-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: #f6f6f6;
  font-family: 'Georgia', serif;
}
.login-card {
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 2px 16px rgba(0,0,0,0.08);
  padding: 48px 36px 36px 36px;
  min-width: 320px;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.login-title {
  font-size: 2rem;
  margin-bottom: 12px;
  color: #28251c;
  letter-spacing: 0.1em;
}
.login-desc {
  font-size: 1rem;
  color: #888;
  margin-bottom: 32px;
  text-align: center;
}
#google-signin-btn {
  margin-bottom: 24px;
}
.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}
.user-info img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
}
.logout-btn {
  margin-left: 16px;
  padding: 6px 16px;
  border-radius: 4px;
  border: none;
  background: #eee;
  cursor: pointer;
  font-size: 1rem;
}
</style>

<div class="login-container">
  <div class="login-card">
    <div class="login-title">Login</div>
    <div class="login-desc">Sign in with your Google account to continue.</div>
    {#if $user}
      {#if showExtraInfo}
        <form on:submit|preventDefault={async () => {
          saving = true;
          error = '';
          console.log('Form submitted', { user: $user, nickname });
          try {
            const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api';
            console.log('About to fetch', `${API_BASE_URL}/users/extra-info`);
            const res = await fetch(`${API_BASE_URL}/users/extra-info`, {
              method: 'POST',
              headers: { 'Content-Type': 'application/json' },
              body: JSON.stringify({
                userId: $user.id,
                nickname
              })
            });
            console.log('Fetch response', res);
            if (!res.ok) {
              console.log('Response not ok', res.status, res.statusText);
              throw new Error('추가 정보 저장 실패');
            }
            const updatedUser = await res.json();
            console.log('Updated user', updatedUser);
            user.set(updatedUser);
            showExtraInfo = false;
            goto('/');
          } catch (e) {
            console.error('Error in form submit', e);
            error = e instanceof Error ? e.message : '오류가 발생했습니다.';
          } finally {
            console.log('Form submit finally block');
            saving = false;
          }
        }}>
          <div class="mb-4">
            <label for="nickname" class="block mb-2 text-gray-700">닉네임을 입력하세요</label>
            <input id="nickname" type="text" bind:value={nickname} class="border rounded px-3 py-2 w-full" required />
          </div>
          {#if error}
            <div class="text-red-500 mb-2">{error}</div>
          {/if}
          <button type="submit" class="bg-indigo-600 text-white px-4 py-2 rounded" disabled={saving}>
            {saving ? '저장 중...' : '저장'}
          </button>
        </form>
      {:else}
        <div class="user-info">
          {#if $user.picture}
            <img src="{$user.picture}" alt="Profile" />
          {/if}
          <span>{$user.name}</span>
          <button class="logout-btn" on:click={logout}>Logout</button>
        </div>
      {/if}
    {:else}
      <div id="google-signin-btn"></div>
    {/if}
  </div>
</div>
