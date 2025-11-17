<template id="search-page-template">
  <h1>Hvor ønsker du å reise?</h1>
  <form @submit.prevent="handleSubmit">
    <label for="start" id="startLabel">Fra:
      <input type="text" id="start" name="start" placeholder="Hvor ønsker du å reise fra?" required>
    </label>
    <label for="end" id="endLabel">Til:
      <input type="text" id="end" name="end" placeholder="Hvor ønsker du å reise til?" required>
    </label>
    <label for="datetime" id="datetimeLabel">Tid:
      <input type="datetime-local" id="datetime" name="datetime">
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
        <article v-for="(trip, index) in results" :key="index">

          <div v-if="trip.legSize > 1">
            <div v-if="trip.transportMode === 'foot'">
              <h3>{{ index + 1 }}. Transporttype: {{ trip.transportMode }}</h3>
              <p><strong>Fra: </strong> {{ trip.startStop }}</p>
              <p><strong>Til: </strong> {{ trip.endStop }}</p>
              <p><strong>Start tid: </strong> {{ trip.legStartTime }}</p>
              <p><strong>Slutt tid: </strong> {{ trip.legEndTime }}</p>
            </div>
            <div v-else>
              <h3>{{ index + 1 }}. Transporttype: {{ trip.transportMode }}</h3>
              <p><strong>Fra: </strong> {{ trip.startStop }}</p>
              <p><strong>Til: </strong> {{ trip.endStop }}</p>
              <p><strong>Transportlinje: </strong> {{ trip.routeName }}</p>
              <p><strong>Transportkode: </strong> {{ trip.publicCode }}</p>
              <p><strong>Operatør: </strong> {{ trip.authorityName }}</p>
              <p><strong>Start tid: </strong> {{ trip.legStartTime }}</p>
              <p><strong>Slutt tid: </strong> {{ trip.legEndTime }}</p>
            </div>
          </div>

          <div v-else>
            <h3>Transporttype: {{ trip.transportMode }}</h3>
            <p><strong>Fra: </strong> {{ trip.startStop }}</p>
            <p><strong>Til: </strong> {{ trip.endStop }}</p>
            <p><strong>Transportlinje: </strong> {{ trip.routeName }}</p>
            <p><strong>Transportkode: </strong> {{ trip.publicCode }}</p>
            <p><strong>Operatør: </strong> {{ trip.authorityName }}</p>
            <p><strong>Start tid: </strong> {{ trip.startTime }}</p>
            <p><strong>Slutt tid: </strong> {{ trip.endTime }}</p>
            <p><strong>Varighet: </strong> {{ trip.duration }} minutter</p>
          </div>
      </article>
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
      const dateTime = document.querySelector("#datetime").value;

      axios.post('/api/search', { start: startStop, end: endStop, transportType: transportTypes, datetime: dateTime })
          .then(response => {
            console.log(transportTypes)
            console.log(response.data);
            if (Array.isArray(response.data) && response.data.length > 0) {
              this.results = response.data;
              this.error = null;
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
    }
  }
});
</script>