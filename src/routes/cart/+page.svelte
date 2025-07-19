<script lang="ts">
  import { onMount } from 'svelte';
  import { goto } from '$app/navigation';
  import { user } from '$lib/stores/auth';
  import { get } from 'svelte/store';

  let cartItems: {
    id: number;
    name: string;
    price: number;
    image: string;
    quantity: number;
  }[] = [];

  $: total = cartItems.reduce((sum, item) => sum + item.price * item.quantity, 0);

  onMount(async () => {
    const currentUser = get(user);
    if (!currentUser) {
      goto('/login');
      return;
    }
    try {
      const res = await fetch(`/cart?userId=${currentUser.id}`);
      if (!res.ok) throw new Error('Failed to fetch cart');
      const data = await res.json();
      cartItems = data.map((item: any) => ({
        id: item.id,
        name: item.productName,
        price: item.price,
        image: item.imageUrl || '/static/garden-bg.jpg',
        quantity: item.quantity
      }));
    } catch (e) {
      cartItems = [];
      // Optionally show error to user
    }
  });
</script>

<div class="cart-container">
  <h1>장바구니</h1>
  {#if cartItems.length === 0}
    <div class="empty-cart">
      <p>장바구니가 비어 있습니다.</p>
    </div>
  {:else}
    <ul class="cart-list">
      {#each cartItems as item}
        <li class="cart-item">
          <img src={item.image} alt={item.name} class="cart-thumb" />
          <div class="cart-info">
            <div class="cart-name">{item.name}</div>
            <div class="cart-qty">수량: {item.quantity}</div>
            <div class="cart-price">{item.price.toLocaleString()}원</div>
          </div>
        </li>
      {/each}
    </ul>
    <div class="cart-total">
      <span>총 합계</span>
      <span class="total-price">{total.toLocaleString()}원</span>
    </div>
    <button class="checkout-btn">주문하기</button>
  {/if}
</div>

<style>
.cart-container {
  max-width: 600px;
  margin: 120px auto 40px auto;
  background: #f3ede2;
  border-radius: 18px;
  box-shadow: 0 4px 24px 0 rgba(40,37,28,0.08);
  padding: 36px 32px 32px 32px;
  font-family: 'Playfair Display', 'Noto Sans KR', serif;
  color: #28251c;
}
.cart-container h1 {
  font-size: 2.2rem;
  font-family: 'Playfair Display', serif;
  margin-bottom: 32px;
  letter-spacing: 0.04em;
  text-align: center;
}
.cart-list {
  list-style: none;
  padding: 0;
  margin: 0 0 32px 0;
}
.cart-item {
  display: flex;
  align-items: center;
  margin-bottom: 24px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px 0 rgba(40,37,28,0.04);
  padding: 16px 18px;
  transition: box-shadow 0.2s;
}
.cart-item:hover {
  box-shadow: 0 4px 16px 0 rgba(40,37,28,0.10);
}
.cart-thumb {
  width: 72px;
  height: 72px;
  object-fit: cover;
  border-radius: 10px;
  margin-right: 22px;
  border: 1.5px solid #e5e0d6;
  background: #f3ede2;
}
.cart-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 6px;
}
.cart-name {
  font-size: 1.15rem;
  font-weight: 600;
  margin-bottom: 2px;
}
.cart-qty {
  font-size: 0.98rem;
  color: #7a7463;
}
.cart-price {
  font-size: 1.05rem;
  color: #b48a5a;
  font-weight: 500;
}
.cart-total {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 1.18rem;
  font-weight: 600;
  margin-bottom: 24px;
  border-top: 1.5px solid #e5e0d6;
  padding-top: 18px;
}
.total-price {
  color: #b48a5a;
}
.checkout-btn {
  width: 100%;
  padding: 16px 0;
  background: linear-gradient(90deg, #b48a5a 0%, #7a7463 100%);
  color: #fff;
  font-size: 1.15rem;
  font-family: 'Noto Sans KR', sans-serif;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  font-weight: 600;
  letter-spacing: 0.04em;
  transition: background 0.2s;
}
.checkout-btn:hover {
  background: linear-gradient(90deg, #7a7463 0%, #b48a5a 100%);
}
.empty-cart {
  text-align: center;
  color: #7a7463;
  font-size: 1.1rem;
  margin: 60px 0;
}
@media (max-width: 700px) {
  .cart-container {
    padding: 18px 6px 18px 6px;
    margin: 90px 4px 24px 4px;
  }
  .cart-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
    padding: 12px 8px;
  }
  .cart-thumb {
    margin-right: 0;
    margin-bottom: 8px;
  }
}
</style>
