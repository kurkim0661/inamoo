<script context="module" lang="ts">
  declare global {
    interface Window {
      daum?: {
        Postcode: new (options: { oncomplete: (data: any) => void }) => { open: () => void };
      };
    }
  }
</script>

<script lang="ts">
  import { onMount } from 'svelte';
  import { goto } from '$app/navigation';
  import { user, logout } from '../../lib/stores/auth';
  import { get } from 'svelte/store';

  let nickname = '';
  let gender = '';
  let birthDate = '';
let address = '';
let postcode = '';
let phoneNumber = '';
let phoneVerificationCode = '';
let phoneVerificationSent = false;
let phoneVerificationSuccess = false;
let phoneVerificationError = '';
let phoneVerificationLoading = false;
let profileImage = '';
let error = '';
let success = '';

  // 기본 프로필 정보
  let name = '';
  let email = '';
  let googleProfileImage = '';

  let postcodeLoading = true;
  let daumPostcodeLoaded: Promise<void>;

  onMount(() => {
    const u = get(user);
    if (u) {
      name = u.name || '';
      email = u.email || '';
      googleProfileImage = u.picture || '';
      // 필요시 nickname 등도 u에서 가져올 수 있음
    }
    // 다음 우편번호 스크립트 로드 Promise 관리
    daumPostcodeLoaded = new Promise<void>((resolve, reject) => {
      if (window.daum && window.daum.Postcode) {
        postcodeLoading = false;
        resolve();
        return;
      }
      const script = document.createElement('script');
      script.src = '//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js';
      script.async = true;
      script.onload = () => {
        postcodeLoading = false;
        resolve();
      };
      script.onerror = reject;
      document.body.appendChild(script);
    });
  });

  async function openPostcode() {
    if (postcodeLoading) {
      alert('주소 검색 스크립트가 아직 로드 중입니다. 잠시 후 다시 시도해주세요.');
      return;
    }
    await daumPostcodeLoaded;
    if (window.daum && window.daum.Postcode) {
      new window.daum.Postcode({
        oncomplete: function(data: any) {
          // 도로명/지번 주소 모두 지원
          address = data.roadAddress || data.jibunAddress || '';
          postcode = data.zonecode || '';
        }
      }).open();
    } else {
      alert('주소 검색 스크립트가 로드되지 않았습니다. 새로고침 후 다시 시도해주세요.');
    }
  }

  function handleLogout() {
    logout();
    goto('/');
  }

  async function submit() {
    error = '';
    success = '';
    try {
      const res = await fetch('/api/users/extra-info', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          nickname,
          gender,
          birthDate,
      address,
      postcode,
      phoneNumber,
      profileImage
      // 전화번호 인증 여부는 서버에 저장하지 않음(프론트에서만 체크)
        })
      });
      if (!res.ok) {
        const data = await res.json();
        error = data.message || '정보 저장에 실패했습니다.';
        return;
      }
      success = '정보가 성공적으로 저장되었습니다.';
      setTimeout(() => goto('/'), 1500);
    } catch (e) {
      error = '네트워크 오류가 발생했습니다.';
    }
  }
</script>

<div class="max-w-2xl mx-auto mt-10 p-6 bg-white rounded shadow">
  <!-- 프로필 카드 -->
  <div class="flex items-center gap-6 border-b pb-6 mb-6">
    <img
      class="w-24 h-24 rounded-full border object-cover"
      src={profileImage || googleProfileImage || 'https://via.placeholder.com/96'}
      alt="프로필 이미지"
    />
    <div class="flex-1">
      <div class="text-xl font-bold">{name || '이름 없음'}</div>
      <div class="text-gray-600">{email || '이메일 없음'}</div>
      <button
        class="mt-2 px-4 py-1 bg-gray-200 rounded hover:bg-gray-300 text-sm"
        on:click={handleLogout}
      >
        로그아웃
      </button>
    </div>
  </div>

  <!-- 내 정보 수정 폼 -->
  <h2 class="text-lg font-semibold mb-4">내 정보 수정</h2>
  <form on:submit|preventDefault={submit} class="space-y-4">
    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
      <div>
        <label class="block mb-1 font-medium">닉네임</label>
        <input class="w-full border rounded px-3 py-2" bind:value={nickname} required />
      </div>
      <div>
        <label class="block mb-1 font-medium">성별</label>
        <select class="w-full border rounded px-3 py-2" bind:value={gender} required>
          <option value="" disabled selected>선택</option>
          <option value="male">남성</option>
          <option value="female">여성</option>
          <option value="other">기타</option>
        </select>
      </div>
      <div>
        <label class="block mb-1 font-medium">생년월일</label>
        <input class="w-full border rounded px-3 py-2" type="date" bind:value={birthDate} required />
      </div>
      <div>
        <label class="block mb-1 font-medium">주소</label>
        <div class="flex flex-col gap-2">
          <div class="flex gap-2">
            <input
              class="w-40 border rounded px-3 py-2"
              placeholder="우편번호"
              bind:value={postcode}
              readonly
              required
            />
            <button
              type="button"
              class="flex items-center gap-1 bg-white border px-3 py-2 rounded hover:bg-blue-50 text-blue-600 font-semibold shadow-sm transition"
              on:click={openPostcode}
              disabled={postcodeLoading}
              style="min-width:120px"
            >
              <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <circle cx="11" cy="11" r="7" stroke-width="2" />
                <line x1="16" y1="16" x2="21" y2="21" stroke-width="2" />
              </svg>
              {postcodeLoading ? '우편번호 찾기(로딩중)' : '우편번호 찾기'}
            </button>
          </div>
          <input
            class="w-full border rounded px-3 py-2"
            placeholder="주소"
            bind:value={address}
            required
            readonly
          />
        </div>
      </div>
      <div>
        <label class="block mb-1 font-medium">전화번호</label>
        <div class="flex gap-2">
          <input
            class="w-full border rounded px-3 py-2"
            type="tel"
            bind:value={phoneNumber}
            required
            placeholder="휴대폰 번호 (- 없이 입력)"
            disabled={phoneVerificationSuccess}
          />
          <button
            type="button"
            class="bg-blue-600 text-white px-3 py-2 rounded font-bold hover:bg-blue-700 text-sm"
            on:click={async () => {
              phoneVerificationError = '';
              phoneVerificationLoading = true;
              // 실제로는 서버에 SMS 요청 API 호출 필요
              await new Promise(r => setTimeout(r, 800));
              phoneVerificationSent = true;
              phoneVerificationLoading = false;
            }}
            disabled={phoneVerificationLoading || phoneVerificationSuccess || !phoneNumber}
          >
            {phoneVerificationLoading ? '전송중...' : phoneVerificationSent ? '재전송' : '인증번호 받기'}
          </button>
        </div>
        {#if phoneVerificationSent && !phoneVerificationSuccess}
          <div class="flex gap-2 mt-2">
            <input
              class="w-full border rounded px-3 py-2"
              type="text"
              bind:value={phoneVerificationCode}
              maxlength="6"
              placeholder="인증번호 입력"
            />
            <button
              type="button"
              class="bg-gray-100 border px-3 py-2 rounded hover:bg-gray-200 text-sm"
              on:click={async () => {
                phoneVerificationError = '';
                phoneVerificationLoading = true;
                // 실제로는 서버에 인증번호 검증 API 호출 필요
                await new Promise(r => setTimeout(r, 800));
                if (phoneVerificationCode === '123456') {
                  phoneVerificationSuccess = true;
                  phoneVerificationError = '';
                } else {
                  phoneVerificationError = '인증번호가 올바르지 않습니다.';
                }
                phoneVerificationLoading = false;
              }}
              disabled={phoneVerificationLoading || !phoneVerificationCode}
            >
              {phoneVerificationLoading ? '확인중...' : '확인'}
            </button>
          </div>
          {#if phoneVerificationError}
            <div class="text-red-500 text-sm mt-1">{phoneVerificationError}</div>
          {/if}
        {/if}
        {#if phoneVerificationSuccess}
          <div class="text-green-600 text-sm mt-1">인증 완료</div>
        {/if}
      </div>
      <div>
        <label class="block mb-1 font-medium">프로필 이미지 URL</label>
        <input class="w-full border rounded px-3 py-2" type="url" bind:value={profileImage} />
      </div>
    </div>
    {#if error}
      <div class="text-red-500">{error}</div>
    {/if}
    {#if success}
      <div class="text-green-500">{success}</div>
    {/if}
    <button class="w-full bg-blue-600 text-white py-2 rounded font-bold hover:bg-blue-700" type="submit">
      저장
    </button>
  </form>

  <!-- 주문내역/배송지 등 e-commerce 마이페이지 섹션 (placeholder) -->
  <div class="mt-10">
    <h3 class="text-lg font-semibold mb-2">주문 내역</h3>
    <div class="bg-gray-50 border rounded p-4 text-gray-500 text-sm">
      주문 내역이 없습니다.
    </div>
  </div>
</div>
