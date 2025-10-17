<template id="search-page-template">
    <h1>Hvor ønsker du å reise?</h1>
    <form action="" method="get">
      <label for="start" id="startLabel">Fra:
        <input type="text" id="start" placeholder="Hvor ønsker du å reise fra?">
      </label>
      <label for="end" id="endLabel">Til:
        <input type="text" id="end" placeholder="Hvor ønsker du å reise til?">
      </label>

      <!--HAMBURGER-->
      <section id="filtering">
        <div id="filterCollection">
          <div id="group" class="filterButton">
            <button id="ham" class="icon-btn"><i class="fa-solid fa-sliders"></i></button>
          </div>
          <!-- Transportvalg -->
          <div id="menu" class="hidden">
            <button id="close">X</button>
            <label class="transportType">
              Buss
              <input type="checkbox">
            </label>
            <label class="transportType">
              Tog
              <input type="checkbox">
            </label>
            <label class="transportType">
              Trikk
              <input type="checkbox">
            </label>
            <label class="transportType">
              Ferge
              <input type="checkbox">
            </label>
          </div>
        </div>
      </section>

      <input id="search" type="submit" value="Finn Reise">
    </form>

    <section id="search-results">
      <h2>Søkeresultat</h2>
      <div class="result"></div>
    </section>
</template>
<link rel="stylesheet" href="/css/global-style.css">
<link rel="stylesheet" href="/css/search-page.css">

<script>
  app.component("search-page", {
    template: "#search-page-template"
  });
</script>
