import { describe, it, expect, beforeEach } from 'vitest';
import { render, fireEvent, waitFor } from '@testing-library/svelte';
import LoginPage from './+page.svelte';
import { user } from '$lib/stores/auth';
import { get } from 'svelte/store';

describe('LoginPage (Google SSO)', () => {
  beforeEach(() => {
    user.set(null);
  });

  it('should show Google login button when not authenticated', () => {
    const { getByText, getByTestId } = render(LoginPage);
    expect(getByText('Login')).toBeTruthy();
    expect(getByText('Sign in with your Google account to continue.')).toBeTruthy();
    expect(document.getElementById('google-signin-btn')).toBeTruthy();
  });

  it('should show extra info form if user.nickname is empty', async () => {
    // Simulate SSO login with no nickname
    user.set({
      id: 123,
      name: 'Test User',
      email: 'test@example.com',
      nickname: '',
      picture: '',
      // other fields as needed
    });

    const { getByLabelText, getByText } = render(LoginPage);

    await waitFor(() => {
      expect(getByLabelText('닉네임을 입력하세요')).toBeTruthy();
      expect(getByLabelText('전화번호를 입력하세요')).toBeTruthy();
      expect(getByText('성별을 선택하세요')).toBeTruthy();
    });
  });

  it('should NOT show extra info form if user.nickname is present', async () => {
    user.set({
      id: 123,
      name: 'Test User',
      email: 'test@example.com',
      nickname: 'Tester',
      picture: '',
      // other fields as needed
    });

    const { queryByLabelText } = render(LoginPage);

    await waitFor(() => {
      expect(queryByLabelText('닉네임을 입력하세요')).toBeNull();
    });
  });
});
