<script lang="ts">
  import { writable } from 'svelte/store';

  export let category: string;

  // Products fetched from API
  const products = writable<any[]>([]);
  const loading = writable<boolean>(true);

  import { onMount } from 'svelte';

  // Fetch products from API when category changes
  async function fetchProducts() {
    loading.set(true);
    try {
      const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '';
      const url = `${API_BASE_URL}/api/products`;
      const res = await fetch(url);
      if (res.ok) {
        const data: any[] = await res.json();
        products.set(
          data
            .filter((p: any) => !category || p.category === category)
            .map((p: any) => ({
              ...p,
              image: p.imageUrl || '/static/garden-bg.jpg',
              rating: 5, // fallback, as rating is not in DB
              variants: [], // fallback, as variants are not in DB
              moreVariants: undefined
            }))
        );
      } else {
        products.set([]);
      }
    } catch (e) {
      products.set([]);
    } finally {
      loading.set(false);
    }
  }

  onMount(() => {
    fetchProducts();
  });

  $: if (category) {
    fetchProducts();
  }

  // Mock category info
  const categoryInfo = {
    title: 'Our Summer Collection',
    description:
      'The easy elegance of our summer collection captures the warmth of golden afternoons and nature in full bloom. From handmade lampshades to graceful oak furniture and breezy upholstery in sun-washed linens, each piece reflects the gentle...'
  };
</script>

<style>
  .breadcrumb {
    font-size: 0.95rem;
    color: #888;
    margin-bottom: 1.5rem;
  }
  .collection-header {
    text-align: center;
    margin-bottom: 2rem;
  }
  .collection-title {
    font-size: 2rem;
    font-weight: 500;
    margin-bottom: 0.5rem;
  }
  .collection-desc {
    color: #666;
    font-size: 1.1rem;
    margin-bottom: 0.5rem;
  }
  .controls {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 2rem;
    border-bottom: 1px solid #eee;
    padding-bottom: 0.5rem;
  }
  .products-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
    gap: 2.5rem;
    margin-bottom: 2rem;
  }
  .product-card {
    background: #fff;
    border-radius: 16px;
    overflow: hidden;
    box-shadow: 0 4px 24px rgba(0,0,0,0.07);
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    transition: box-shadow 0.2s, transform 0.2s;
    min-height: 420px;
    position: relative;
  }
  .product-card.enhanced:hover {
    box-shadow: 0 8px 32px rgba(0,0,0,0.13);
    transform: translateY(-4px) scale(1.02);
    z-index: 2;
  }
  .product-image-wrap {
    position: relative;
    width: 100%;
  }
  .product-image {
    width: 100%;
    aspect-ratio: 4/5;
    object-fit: cover;
    background: #f5f5f5;
    border-bottom: 1px solid #f0f0f0;
    transition: filter 0.2s;
  }
  .product-card.enhanced:hover .product-image {
    filter: brightness(0.95) blur(0.5px);
  }
  .product-category {
    position: absolute;
    top: 12px;
    left: 12px;
    background: #f7f7f7cc;
    color: #444;
    font-size: 0.85rem;
    padding: 0.2rem 0.7rem;
    border-radius: 12px;
    box-shadow: 0 1px 4px rgba(0,0,0,0.04);
    font-weight: 500;
    letter-spacing: 0.01em;
  }
  .product-info {
    padding: 1.1rem 1rem 1rem 1rem;
    width: 100%;
    display: flex;
    flex-direction: column;
    gap: 0.4rem;
    flex: 1 1 auto;
  }
  .product-name {
    font-size: 1.13rem;
    font-weight: 600;
    margin-bottom: 0.1rem;
    color: #222;
    line-height: 1.2;
  }
  .product-price {
    color: #1a7f37;
    font-size: 1.08rem;
    font-weight: 500;
    margin-bottom: 0.2rem;
  }
  .product-desc {
    color: #666;
    font-size: 0.98rem;
    margin-bottom: 0.2rem;
    min-height: 2.2em;
    line-height: 1.4;
  }
  .product-meta {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 0.93rem;
    color: #888;
    margin-bottom: 0.3rem;
    gap: 0.5rem;
  }
  .product-date {
    font-size: 0.92rem;
    color: #aaa;
  }
  .product-rating {
    color: #e2b93b;
    font-size: 1.1rem;
    letter-spacing: 0.05em;
  }
  .view-btn {
    margin-top: auto;
    background: linear-gradient(90deg, #e2cfa5 0%, #bfcad6 100%);
    color: #222;
    border: none;
    border-radius: 8px;
    padding: 0.55rem 1.2rem;
    font-size: 1rem;
    font-weight: 500;
    cursor: pointer;
    box-shadow: 0 2px 8px rgba(0,0,0,0.04);
    transition: background 0.2s, color 0.2s, box-shadow 0.2s;
  }
  .view-btn:hover, .view-btn:focus {
    background: linear-gradient(90deg, #bfcad6 0%, #e2cfa5 100%);
    color: #111;
    box-shadow: 0 4px 16px rgba(0,0,0,0.09);
    outline: none;
  }
  /* Skeleton loader styles */
  .skeleton {
    pointer-events: none;
    opacity: 0.7;
    animation: skeleton-fade 1.2s infinite alternate;
  }
  .skeleton-bg {
    background: linear-gradient(90deg, #f3f3f3 25%, #ececec 50%, #f3f3f3 75%);
    background-size: 200% 100%;
    animation: skeleton-bg 1.5s infinite linear;
    border-radius: 6px;
  }
  @keyframes skeleton-bg {
    0% { background-position: 200% 0; }
    100% { background-position: -200% 0; }
  }
  @keyframes skeleton-fade {
    0% { opacity: 0.7; }
    100% { opacity: 0.5; }
  }
  /* Empty state styles */
  .empty-state {
    grid-column: 1 / -1;
    text-align: center;
    padding: 3rem 0 2rem 0;
    color: #888;
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 1.2rem;
  }
  .empty-illustration {
    width: 120px;
    height: 120px;
    opacity: 0.7;
    margin-bottom: 0.5rem;
  }
  .empty-title {
    font-size: 1.25rem;
    font-weight: 600;
    color: #444;
  }
  .empty-desc {
    font-size: 1.02rem;
    color: #888;
    margin-bottom: 0.5rem;
  }
</style>

<div class="breadcrumb">
  <a href="/">Home</a> / <span style="color:#444;">{categoryInfo.title}</span>
</div>

<div class="collection-header">
  <div class="collection-title">{categoryInfo.title}</div>
  <div class="collection-desc">{categoryInfo.description}</div>
  <div style="color:#888; font-size:0.95rem;">
    <a href="#" style="color:#888; text-decoration:underline;">Read more</a>
  </div>
</div>

<div class="controls">
  <div>
    <span>Filter</span>
    <span style="margin-left:0.5rem;">&#8801;</span>
  </div>
  <div>
    <span>Sort by</span>
    <span style="margin-left:0.5rem;">&#9660;</span>
  </div>
  <div style="color:#888; font-size:0.95rem;">
    {$products.length === 0
      ? '0 products'
      : `${$products.length} products`}
  </div>
</div>

<div class="products-grid">
  {#if $loading}
    {#each Array(6) as _, i}
      <div class="product-card skeleton" aria-hidden="true">
        <div class="product-image skeleton-bg"></div>
        <div class="product-info">
          <div class="product-name skeleton-bg" style="width: 70%; height: 1.2rem; margin-bottom: 0.5rem;"></div>
          <div class="product-price skeleton-bg" style="width: 40%; height: 1rem; margin-bottom: 0.5rem;"></div>
          <div class="product-desc skeleton-bg" style="width: 90%; height: 0.9rem; margin-bottom: 0.5rem;"></div>
          <div class="product-meta skeleton-bg" style="width: 50%; height: 0.8rem;"></div>
        </div>
      </div>
    {/each}
  {:else if $products.length === 0}
    <div class="empty-state">
      <img src="/static/empty-box.svg" alt="No products" class="empty-illustration" />
      <div class="empty-title">상품이 없습니다</div>
      <div class="empty-desc">새로운 상품이 곧 추가될 예정입니다. 잠시만 기다려주세요!</div>
    </div>
  {:else}
    {#each $products as product, i (product.id)}
      <div class="product-card enhanced">
        <div class="product-image-wrap">
          <img class="product-image" src={product.image} alt={product.name} />
          <span class="product-category">{product.category}</span>
        </div>
        <div class="product-info">
          <div class="product-name">{product.name}</div>
          <div class="product-price">₩{product.price.toLocaleString()}</div>
          <div class="product-desc">{product.description || '설명이 없습니다.'}</div>
          <div class="product-meta">
            <span class="product-date">
              등록일: {product.createdAt ? new Date(product.createdAt).toLocaleDateString() : '-'}
            </span>
            <span class="product-rating">
              {'★'.repeat(product.rating)}
            </span>
          </div>
          <button class="view-btn" tabindex="0">상세보기</button>
        </div>
      </div>
    {/each}
  {/if}
</div>
