<template id="search-page-template">
    <h1>Hvor ønsker du å reise?</h1>
    <form action="" method="get">
      <label for="start">Fra:
        <input type="text" id="start" placeholder="Hvor ønsker du å reise fra?">
      </label>
      <label for="end">Til:
        <input type="text" id="end" placeholder="Hvor ønsker du å reise til?">
      </label>
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
