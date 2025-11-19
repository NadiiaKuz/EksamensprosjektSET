<template id="create-user-template">
  <form action="/api/users" method="post">

    <h1>Opprett ny bruker</h1>

    <label for="firstName" >Fornavn*:
      <input type="text" id="firstName" name="firstName" placeholder="Kari*" maxlength="45" pattern="[A-Za-zæøåÆØÅ]{2,}"required>
    </label>

    <label for="lastName">Etternavn:
      <input type="text" id="lastName" name="lastName" placeholder="Nordmann" maxlength="45" pattern="[A-Za-zæøåÆØÅ]{2,}">
    </label>

    <label for="mail">E-postadresse*:
      <input type="email" id="mail" name="mail" placeholder="Kari.Nordmann@hiof.no" pattern="[a-z0-9._%+\-]+@[a-z0-9.\-]+\.[a-z]{2,}$" required>
    </label>

    <label for="userType" id="type">Velg reisende:
      <select name="userType" id="userType" required>
        <option value="">-</option>
        <option value="CHILD">Barn</option>
        <option value="STUDENT">Student</option>
        <option value="ADULT">Voksen</option>
        <option value="SENIOR">Honnør</option>
      </select>
    </label>

    <label for="password">Passord
      <input type="password" id="password" name="password" required>
    </label>
    
    <label for="vilkaar" id="terms">
      <input type="checkbox" id="vilkaar" required>
      Ja, jeg godtar vilkårene.
    </label>

    <input id="submit" type="submit" value="Opprett bruker">
  </form>
</template>

<link rel="stylesheet" href="/css/global-style.css">
<link rel="stylesheet" href="/css/create-user.css">
<script>
  app.component("create-user", {
    template: "#create-user-template",
    mounted() {
      const params = new URLSearchParams(window.location.search);
      if (params.get("created") === "true") {
        alert("Bruker ble opprettet!");
      }
    }
  });
</script>