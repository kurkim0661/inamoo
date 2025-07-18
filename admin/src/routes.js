import Dashboard from "./pages/Dashboard.svelte";
import Users from "./pages/Users.svelte";
import Products from "./pages/Products.svelte";
import Payments from "./pages/Payments.svelte";

export default {
  "/": Dashboard,
  "/dashboard": Dashboard,
  "/users": Users,
  "/products": Products,
  "/payments": Payments
};
