<template>
  <div>
    <nav>
      hello？
      <router-link to="/">Home</router-link>
      |
      <router-link to="/about">About</router-link>
      |
      <router-link to="/word">Word</router-link>
      |
      <router-link to="/translate">Translate</router-link>
    </nav>
    <router-view/>
  </div>
</template>

<script>

export default {
  name: "HomeView",
};
</script>
