<script lang="ts">
  // No props needed for main page hero
</script>

<div class="hero-content">
  <div class="collection">OUR GARDEN COLLECTION</div>
  <h1>For Alfresco Moments</h1>
  <a class="shop-link" href="#">SHOP NOW</a>
  <!-- Auth UI removed for main page -->
  <div style="margin-top: 32px;"></div>
</div>

<style>
  .hero-content {
    position: absolute;
    left: 0;
    bottom: 0;
    z-index: 3;
    color: #fff;
    padding: 0 0 60px 60px;
    max-width: 600px;
    text-shadow: 0 2px 8px rgba(0,0,0,0.18);
  }

  .hero-content .collection {
    font-size: 0.9rem;
    letter-spacing: 0.15em;
    margin-bottom: 12px;
    opacity: 0.8;
  }

  .hero-content h1 {
    font-size: 2.5rem;
    font-family: 'Georgia', serif;
    margin: 0 0 18px 0;
    font-weight: 400;
  }

  .hero-content .shop-link {
    display: inline-block;
    margin-top: 18px;
    font-size: 1rem;
    color: #fff;
    border-bottom: 1px solid #fff;
    text-decoration: none;
    letter-spacing: 0.1em;
    opacity: 0.9;
    transition: opacity 0.2s;
  }
  .hero-content .shop-link:hover {
    opacity: 1;
  }

  @media (max-width: 700px) {
    .hero-content {
      padding: 0 0 30px 18px;
      max-width: 95vw;
    }
    .hero-content h1 {
      font-size: 1.5rem;
    }
  }
</style>
