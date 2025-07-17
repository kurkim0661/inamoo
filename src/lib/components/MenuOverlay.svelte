<script lang="ts">
  import { slide } from 'svelte/transition';
  import { goto } from '$app/navigation';
  export let closeMenu: () => void;

  // 카테고리 목록
  const categories = [
    { label: 'NEW & FEATURED', slug: 'new-featured' },
    { label: 'GARDEN', slug: 'garden' },
    { label: 'FURNITURE', slug: 'furniture' },
    { label: 'ACCESSORIES', slug: 'accessories' },
    { label: 'KITCHEN & DINING', slug: 'kitchen-dining' },
    { label: 'BEDROOM', slug: 'bedroom' },
    { label: 'BATHROOM', slug: 'bathroom' },
    { label: 'HARDWARE', slug: 'hardware' },
    { label: 'LIGHTING', slug: 'lighting' },
    { label: 'LAST CHANCE', slug: 'last-chance' }
  ];
</script>

<div class="menu-overlay" transition:slide={{ duration: 320 }}>
  <div class="menu-content">
    <div class="menu-header">
      <span class="close-icon" on:click={closeMenu}>&#10005;</span>
      <span class="menu-title">Inamoo</span>
    </div>
    <ul class="menu-links">
      {#each categories as cat}
        <li>
          <a
            href={"/collections/" + cat.slug}
            class="category-link"
            on:click|preventDefault={() => { goto('/collections/' + cat.slug); closeMenu(); }}
            >{cat.label}</a
          >
        </li>
      {/each}
    </ul>
  </div>
</div>

<style>
  .menu-overlay {
    position: absolute;
    top: 0;
    left: 0;
    width: 100vw;
    min-height: 120px;
    max-height: 80vh;
    height: auto;
    background: #f6f6f6;
    z-index: 10;
    box-shadow: 0 2px 16px rgba(0,0,0,0.04);
    display: flex;
    flex-direction: column;
    overflow-y: auto;
    transition: max-height 0.2s, min-height 0.2s;
  }
  .menu-content {
    width: 100%;
    max-width: 1400px;
    margin: 0 auto;
    padding: 32px 0 0 0;
    display: flex;
    flex-direction: column;
    height: 100%;
  }
  .menu-header {
    display: flex;
    align-items: center;
    margin-bottom: 24px;
    padding: 0 40px;
  }
  .close-icon {
    font-size: 2rem;
    cursor: pointer;
    margin-right: 32px;
    color: #888;
    transition: color 0.2s;
  }
  .close-icon:hover {
    color: #222;
  }
  .menu-title {
    font-size: 2rem;
    font-family: 'Georgia', serif;
    letter-spacing: 0.18em;
    color: #888;
  }
  .menu-links {
    list-style: none;
    padding: 0 0 0 180px;
    margin: 0;
    display: flex;
    flex-direction: column;
    gap: 12px;
  }
  .menu-links li {
    font-size: 1rem;
    letter-spacing: 0.08em;
    padding: 2px 0;
  }
  .category-link {
    color: #888;
    text-decoration: none;
    transition: color 0.2s;
    cursor: pointer;
    display: inline-block;
    width: 100%;
    padding: 2px 0;
  }
  .category-link:hover {
    color: #222;
    text-decoration: underline;
  }
  @media (max-width: 900px) {
    .menu-content {
      padding: 24px 0 0 0;
    }
    .menu-header {
      padding: 0 18px;
    }
    .menu-links {
      padding-left: 30px;
    }
  }
</style>
