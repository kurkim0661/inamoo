<script lang="ts">
  import { onMount } from 'svelte';
  import { user, initGoogleSSO, logout } from '$lib/stores/auth';
  import { goto } from '$app/navigation';

  onMount(() => {
    initGoogleSSO();
  });

  $: if ($user) {
    goto('/');
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
      <div class="user-info">
        {#if $user.picture}
          <img src="{$user.picture}" alt="Profile" />
        {/if}
        <span>{$user.name}</span>
        <button class="logout-btn" on:click={logout}>Logout</button>
      </div>
    {:else}
      <div id="google-signin-btn"></div>
    {/if}
  </div>
</div>
