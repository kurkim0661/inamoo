/// <reference types="@sveltejs/kit" />
// See https://svelte.dev/docs/kit/types#app.d.ts
// for information about these interfaces
declare global {
	namespace App {
		// interface Error {}
		// interface Locals {}
		// interface PageData {}
		// interface PageState {}
		// interface Platform {}
	}
}

export {};

declare module 'flowbite-svelte-admin-dashboard/AppsMenu.svelte' {
  import { SvelteComponentTyped } from 'svelte';
  export default class AppsMenu extends SvelteComponentTyped<any, any, any> {}
}
declare module 'flowbite-svelte-admin-dashboard/CardWidget.svelte' {
  import { SvelteComponentTyped } from 'svelte';
  export default class CardWidget extends SvelteComponentTyped<any, any, any> {}
}
