<template id="search-page-template">
  <h1>Hvor ønsker du å reise?</h1>
  <form @submit.prevent="handleSubmit">
    <label for="start" id="startLabel">Fra:
      <input type="text" id="start" name="start" placeholder="Hvor ønsker du å reise fra?" required>
    </label>
    <label for="end" id="endLabel">Til:
      <input type="text" id="end" name="end" placeholder="Hvor ønsker du å reise til?" required>
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
            <input type="checkbox" name="transportType" value="BUS">
          </label>
          <label class="transportType">
            Tog
            <input type="checkbox" name="transportType" value="RAIL">
          </label>
          <label class="transportType">
            Trikk
            <input type="checkbox" name="transportType" value="TRAM">
          </label>
          <label class="transportType">
            Ferge
            <input type="checkbox" name="transportType" value="FERRY">
          </label>
        </div>
      </div>
    </section>

    <button id="search" type="submit">Finn din reise</button>
  </form>

  <section id="search-results">
    <h2>Søkeresultat</h2>
    <div class="result"></div>
    <div v-if="error" class="error">{{ error }}</div>
    <div v-if="results.length">
      <ul>
        <li v-for="result in results" :key="result.routeId" class="route-info">
          <h3>Transporttype: {{ result.transportType }}</h3>
          <p><strong>Operatør:</strong> {{ result.operatorName }}</p>
          <p><strong>Startstopp:</strong> {{ result.startStop }}</p>
          <p><strong>Sluttstopp:</strong> {{ result.endStop }}</p>
          <p><strong>Start tid:</strong> {{ result.startTime }}</p>
          <p><strong>Slutt tid</strong> {{ result.endTime }}</p>
          <p><strong>Varighet:</strong> {{ result.duration }} sekunder</p>
          <p><strong>Rutekode:</strong>  {{ result.publicCode }}</p>
        </li>
      </ul>
    </div>
    <div v-else>
      <p>Ingen ruteinformasjon tilgjengelig</p>
    </div>
  </section>
</template>
<link rel="stylesheet" href="/css/global-style.css">
<link rel="stylesheet" href="/css/search-page.css">

<script>
app.component("search-page", {
  data() {
    return {
      results: [],
      error: null
    };
  },
  template: "#search-page-template",
  methods: {
    handleSubmit() {
      const startStop = document.querySelector("#start").value;
      const endStop = document.querySelector("#end").value;
      const transportTypes = Array.from(document.querySelectorAll("input[name='transportType']:checked"))
          .map(input => input.value);

      axios.post('/api/search', { start: startStop, end: endStop, transportType: transportTypes })
          .then(response => {
            console.log(response.data);

            if (Array.isArray(response.data) && response.data.length > 0) {
              this.results = response.data;
            } else {
              this.results = [];
              this.error = 'Fant ingen reisedata';
            }
            this.error = null;
          })
          .catch(error => {
            console.error("API Feil: ", error);
            this.error = 'En feil oppstod: ' + error.message;
            this.results = [];
          });


            // for debugging
            //console.log(response.data);
            // Tildeler data til results hvis alt fungerer korrekt
            //this.results = response.data;
            // Nullstiller error hvis forespørselen er vellykket
            //this.error = null;
         // })
         // .catch(error => {
            // Logger feilmeldingen i konsollen for debugging
          //  console.error(error);
            // this.error = 'En feil oppstod: ' + error.message;
          //})
    }
  }
});
</script>