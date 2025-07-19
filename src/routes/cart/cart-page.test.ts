import { render, waitFor } from '@testing-library/svelte';
import CartPage from './+page.svelte';
import { user } from '$lib/stores/auth';
import { get } from 'svelte/store';
import { vi, describe, it, expect, beforeEach } from 'vitest';

// Mock user store to simulate authenticated user
vi.mock('$lib/stores/auth', () => ({
  user: {
    subscribe: (run: any) => {
      run({ id: 123, email: 'test@example.com' });
      return () => {};
    }
  }
}));

describe('CartPage', () => {
  beforeEach(() => {
    vi.stubGlobal('fetch', vi.fn(() =>
      Promise.resolve({
        ok: true,
        json: () =>
          Promise.resolve([
            {
              id: 1,
              productId: 10,
              productName: 'Test Product',
              price: 50000,
              quantity: 2,
              imageUrl: '/static/test.jpg'
            }
          ])
      })
    ));
  });

  it('renders cart items from backend', async () => {
    const { getByText, getByAltText } = render(CartPage);

    await waitFor(() => {
      expect(getByText('Test Product')).toBeInTheDocument();
      expect(getByText('수량: 2')).toBeInTheDocument();
      expect(getByText('50,000원')).toBeInTheDocument();
      expect(getByAltText('Test Product')).toHaveAttribute('src', '/static/test.jpg');
    });
  });
});
