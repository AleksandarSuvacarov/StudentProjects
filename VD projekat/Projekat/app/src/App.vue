<template>
  <div>
    <Header  @languageChange="updateTranslation()"></Header>
    <router-view :key="$route.fullPath" />
    <Footer></Footer>
  </div>
</template>

<style>

/* ovde jedino pravi problem uvozenje tkd nek stoji ovako bez */

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  /* color: #2c3e50; */
  background-color: #fef9f2;
  height: 100vh;
}

/* div{
  background-color: #fef9f2;
} */

nav {
  /* padding: 30px; */
  text-decoration: none;
  font-family: Source Sans Pro;
}

nav a {
  /* font-weight: bold;
  color: #2c3e50; */
  text-decoration: none;
}

nav a.router-link-exact-active {
  /* color: #42b983; */
  text-decoration: none;
}
</style>


<script>

  import Header from './components/HeaderComp.vue'
  import Footer from './components/FooterView.vue'
  export default ({
    name: 'App',
    components:{
      Header,
      Footer
    },
    methods:{
      updateTranslation(){
        this.$forceUpdate();
      }
    }
  })
</script>
