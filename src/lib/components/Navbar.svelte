<script lang="ts">
  import { user } from '../stores/auth';
  import { goto } from '$app/navigation';
  import { get } from 'svelte/store';
  export let openMenu: () => void;

  function handleAccountClick() {
    if (get(user)) {
      goto('/profile');
    } else {
      goto('/login');
    }
  }

  function handleCartClick() {
    if (get(user)) {
      goto('/cart');
    } else {
      goto('/login');
    }
  }
</script>

<nav class="navbar">
  <div class="left">
    <span style="font-size:0.95rem; margin-right:18px;">SHOP NOW</span>
    <span class="menu-icon" on:click={openMenu}>&#9776;</span>
  </div>
  <div class="center">
    <div class="inamoo-logo-wrapper">
      <svg class="inamoo-logo" viewBox="0 0 320 120" fill="none" xmlns="http://www.w3.org/2000/svg">
        <circle cx="160" cy="60" r="58" fill="#f3ede2" stroke="#e5e0d6" stroke-width="3"/>
        <text x="38" y="80" font-family="'Playfair Display', Georgia, serif" font-size="54" fill="#28251c" letter-spacing="2">ina</text>
        <text x="120" y="80" font-family="'Playfair Display', Georgia, serif" font-size="54" fill="#28251c" letter-spacing="2">m</text>
        <path d="M180 80
          C190 60, 230 60, 240 80
          C250 100, 210 100, 200 80
          Z" fill="#28251c"/>
        <path d="M240 80
          C250 60, 290 60, 300 80
          C310 100, 270 100, 260 80
          Z" fill="#28251c"/>
      </svg>
    </div>
  </div>
  <div class="right">
    <span title="Search">&#128269;</span>
    <span
      title="Account"
      style="color:inherit;text-decoration:none;"
      on:click={handleAccountClick}
      ><span>&#128100;</span></span>
    <span
      title="Cart"
      class="cart-icon"
      on:click={handleCartClick}
      >&#128722;</span>
  </div>
</nav>

<style>
  .navbar {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    z-index: 2;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 32px 40px 0 20px;
    color: #fff;
    font-size: 1.1rem;
    letter-spacing: 0.1em;
  }

  .navbar .left,
  .navbar .center,
  .navbar .right {
    display: flex;
    align-items: center;
  }

  .navbar .center {
    flex: 1;
    justify-content: center;
    font-size: 2rem;
    font-family: 'Georgia', serif;
    letter-spacing: 0.2em;
    background: none;
  }

  .navbar .right > * {
    margin-left: 24px;
    cursor: pointer;
    opacity: 0.85;
    transition: opacity 0.2s;
  }
  .navbar .right > *:hover {
    opacity: 1;
  }

  .menu-icon {
    margin-right: 18px;
    font-size: 1.5rem;
    cursor: pointer;
  }

  .cart-icon {
    position: relative;
    font-size: 1.45rem;
    color: #b48a5a;
    transition: color 0.2s, transform 0.2s;
  }
  .cart-icon:hover {
    color: #7a7463;
    transform: scale(1.13) rotate(-8deg);
    text-shadow: 0 2px 8px rgba(180,138,90,0.12);
  }

  .inamoo-logo-wrapper {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 180px;
    height: 180px;
    margin: 0 auto;
  }

  .inamoo-logo {
    width: 180px;
    height: 180px;
    display: block;
  }

  @media (max-width: 700px) {
    .navbar {
      flex-direction: column;
      align-items: flex-start;
      padding: 18px 10px 0 10px;
    }
    .navbar .center {
      font-size: 1.5rem;
    }
  }
</style>
