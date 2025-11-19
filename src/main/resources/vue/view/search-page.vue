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
      <article v-for="(tripPattern, index) in results" :key="index">

        <div v-if="tripPattern.legs.length > 1">
          <!-- Hvis turen har flere "legs", vises start- og sluttid for hele turen -->
          <h3>Reise Start: {{ tripPattern.aimedStartTime }}</h3>
          <h3>Reise Slutt: {{ tripPattern.aimedEndTime }}</h3>
          <h3>Varighet: {{ tripPattern.tripDuration }} minutter</h3>
        </div>

        <div v-for="(leg, legIndex) in tripPattern.legs" :key="legIndex" class="leg-item">

          <div v-if="tripPattern.legs.length > 1">
            <h3>Tur {{ legIndex + 1 }}</h3>
          </div>

          <div v-if="leg.transportMode !== 'foot'">
            <h3>Linje: {{ leg.publicCode }} - {{ leg.routeName }}</h3>
            <p><strong>Fra: </strong> {{ leg.startStop }}</p>
            <p><strong>Til: </strong> {{ leg.endStop }}</p>
            <p><strong>Start tid: </strong> {{ leg.legStartTime }}</p>
            <p><strong>Slutt tid: </strong> {{ leg.legEndTime }}</p>
            <p><strong>Operatør: </strong> {{ leg.authorityName }}</p>
            <p><strong>Transporttype: </strong>{{ leg.transportMode }}</p>
          </div>

          <div v-else-if="leg.transportMode === 'foot'">
            <h3>Gå til fots</h3>
            <p><strong>Fra: </strong> {{ leg.startStop }}</p>
            <p><strong>Til: </strong> {{ leg.endStop }}</p>
            <p><strong>Start tid: </strong> {{ leg.legStartTime }}</p>
            <p><strong>Slutt tid: </strong> {{ leg.legEndTime }}</p>
            <p><strong>Avstand: </strong> {{ leg.distance }} meter</p>
          </div>
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