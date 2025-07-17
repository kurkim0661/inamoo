<script lang="ts">
  import Navbar from '$lib/components/Navbar.svelte';
  import MenuOverlay from '$lib/components/MenuOverlay.svelte';
  import HeroContent from '$lib/components/HeroContent.svelte';

  let menuOpen = false;
  function openMenu() {
    menuOpen = true;
  }
  function closeMenu() {
    menuOpen = false;
  }
</script>

<style>
.main-hero {
  position: relative;
  width: 100vw;
  height: 100vh;
  min-height: 100vh;
  overflow: hidden;
  font-family: 'Georgia', serif;
}

.bg-image {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  z-index: 1;
  filter: brightness(0.85);
}
</style>

<div class="main-hero">
  <img
    class="bg-image"
    src="/garden-bg.jpg"
    alt="Garden background"
  />

  <Navbar {openMenu} />

  {#if menuOpen}
    <MenuOverlay {closeMenu} />
  {/if}

  <HeroContent />
</div>
