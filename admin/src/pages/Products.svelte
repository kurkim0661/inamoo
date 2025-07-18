<script>
  import { onMount } from "svelte";
  import { apiGet, apiPost } from "../api.js";

  let products = [];
  let loading = true;
  let error = "";

  // Add product modal state
  let showAddModal = false;
  let newProduct = { name: "", price: "", stock: "", status: "판매중" };
  let addLoading = false;
  let addError = "";
  let imageFile = null;
  let imagePreview = "";

  async function fetchProducts() {
    loading = true;
    error = "";
    try {
      products = await apiGet("/products");
    } catch (e) {
      error = "상품 정보를 불러오지 못했습니다.";
    } finally {
      loading = false;
    }
  }

  async function addProduct() {
    addLoading = true;
    addError = "";
    try {
      const formData = new FormData();
      formData.append("name", newProduct.name);
      formData.append("price", newProduct.price);
      formData.append("stock", newProduct.stock);
      formData.append("status", newProduct.status);
      if (imageFile) {
        formData.append("image", imageFile);
      }
      const res = await fetch(import.meta.env.VITE_API_URL + "/products", {
        method: "POST",
        body: formData
      });
      if (!res.ok) throw new Error(await res.text());
      showAddModal = false;
      newProduct = { name: "", price: "", stock: "", status: "판매중" };
      imageFile = null;
      imagePreview = "";
      await fetchProducts();
    } catch (e) {
      addError = "상품 등록에 실패했습니다.";
    } finally {
      addLoading = false;
    }
  }

  onMount(fetchProducts);
</script>

<div class="p-8">
  <h1 class="text-2xl font-bold mb-6 text-gray-800">상품 관리</h1>
  <button
    class="mb-4 px-4 py-2 bg-indigo-600 text-white rounded-lg shadow hover:bg-indigo-700 transition"
    on:click={() => showAddModal = true}
  >
    + 상품 추가
  </button>

  <!-- Add Product Modal -->
  {#if showAddModal}
    <div class="fixed inset-0 bg-black bg-opacity-30 flex items-center justify-center z-50">
      <div class="bg-white rounded-xl shadow-lg p-8 w-full max-w-md relative">
        <button class="absolute top-3 right-3 text-gray-400 hover:text-gray-600 text-xl" on:click={() => showAddModal = false}>&times;</button>
        <h2 class="text-xl font-bold mb-4 text-gray-800">상품 추가</h2>
        <form on:submit|preventDefault={addProduct}>
          <div class="mb-3">
            <label class="block mb-1 text-gray-700" for="add-name">상품명</label>
            <input id="add-name" class="w-full border rounded px-3 py-2" bind:value={newProduct.name} required />
          </div>
          <div class="mb-3">
            <label class="block mb-1 text-gray-700" for="add-price">가격</label>
            <input id="add-price" class="w-full border rounded px-3 py-2" type="number" min="0" bind:value={newProduct.price} required />
          </div>
          <div class="mb-3">
            <label class="block mb-1 text-gray-700" for="add-stock">재고</label>
            <input id="add-stock" class="w-full border rounded px-3 py-2" type="number" min="0" bind:value={newProduct.stock} required />
          </div>
          <div class="mb-3">
            <label class="block mb-1 text-gray-700" for="add-image">이미지</label>
            <input
              id="add-image"
              class="w-full border rounded px-3 py-2"
              type="file"
              accept="image/*"
              on:change={e => {
                const input = e.currentTarget;
                if (input && input.files && input.files[0]) {
                  imageFile = input.files[0];
                  const reader = new FileReader();
                  reader.onload = ev => {
                    if (typeof ev.target.result === "string") {
                      imagePreview = ev.target.result;
                    } else {
                      imagePreview = "";
                    }
                  };
                  reader.readAsDataURL(imageFile);
                } else {
                  imageFile = null;
                  imagePreview = "";
                }
              }}
            />
            {#if imagePreview}
              <img src={imagePreview} alt="미리보기" class="mt-2 h-24 object-contain rounded border" />
            {/if}
          </div>
          <div class="mb-4">
            <label class="block mb-1 text-gray-700" for="add-status">상태</label>
            <select id="add-status" class="w-full border rounded px-3 py-2" bind:value={newProduct.status} required>
              <option value="판매중">판매중</option>
              <option value="품절">품절</option>
            </select>
          </div>
          {#if addError}
            <div class="mb-2 text-red-500 text-sm">{addError}</div>
          {/if}
          <button
            class="w-full py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition disabled:opacity-50"
            type="submit"
            disabled={addLoading}
          >
            {addLoading ? "등록 중..." : "상품 등록"}
          </button>
        </form>
      </div>
    </div>
  {/if}

  <div class="overflow-x-auto">
    {#if loading}
      <div class="p-8 text-center text-gray-400">로딩 중...</div>
    {:else if error}
      <div class="p-8 text-center text-red-500">{error}</div>
    {:else}
      <table class="min-w-full bg-white rounded-xl shadow-md">
        <thead>
          <tr class="bg-indigo-50">
            <th class="py-3 px-4 text-left font-semibold text-gray-700">상품명</th>
            <th class="py-3 px-4 text-left font-semibold text-gray-700">가격</th>
            <th class="py-3 px-4 text-left font-semibold text-gray-700">재고</th>
            <th class="py-3 px-4 text-left font-semibold text-gray-700">상태</th>
            <th class="py-3 px-4"></th>
          </tr>
        </thead>
        <tbody>
          {#each products as product}
            <tr class="border-b hover:bg-indigo-50 transition">
              <td class="py-2 px-4">{product.name}</td>
              <td class="py-2 px-4">{product.price}</td>
              <td class="py-2 px-4">{product.stock}</td>
              <td class="py-2 px-4">
                <span class="px-2 py-1 rounded-full text-xs font-semibold {product.status === '판매중' ? 'bg-green-100 text-green-600' : 'bg-gray-200 text-gray-500'}">
                  {product.status}
                </span>
              </td>
              <td class="py-2 px-4">
                <button class="text-indigo-600 hover:underline text-sm">수정</button>
              </td>
            </tr>
          {/each}
        </tbody>
      </table>
    {/if}
  </div>
</div>
