<template>
    <div class="login">
        <form>
            <!-- Email input -->
            <div class="form-outline mb-4">
              <input type="email" id="form2Example1" class="form-control" v-model="username"/>
              <label class="form-label" for="form2Example1">{{$t("Korisnicko ime")}}</label>
            </div>
            <!-- Password input -->
            <div class="form-outline mb-4">
              <input type="password" id="form2Example2" class="form-control" v-model="password"/>
              <label class="form-label" for="form2Example2">{{$t("Lozinka")}}</label>
            </div>
            <!-- Submit button -->
            <button type="button" class="btn global-button btn-block mb-4" @click="prijaviSe()">{{$t("Prijavi se")}}</button>
            <div id="poruka_o_gresci">
              {{poruka_o_gresci}}
            </div>
        </form>
    </div>
    

</template>

<style lang='less' scoped>

  @import "../globalniStilovi.less";

  .login{
    margin: 5%;
    border: solid black 3px;
    padding: 5%;
    width: 55vw;
    // display: flex;
    // justify-content: center;
    margin: auto;
    margin-bottom: 100px;
  }




</style>







<script>
    import svi_korisnici from '../data/korisnici_data.js'
    import $ from 'jquery';
    // import eventBus from "../data/eventBus.js"

    export default {
        name: 'UlogujSe',    
        methods:{
            prijaviSe(){
            var user = svi_korisnici.find(user=>user.username==this.username && user.password==this.password)
            if(user) {
                localStorage.setItem('ulogovan_korisnik', JSON.stringify(user))
                localStorage.setItem('ulogovan', true);
                $('#ulogujse').hide();
                $('#izlogujse').show();
                $('#mojnalog').show();

                
                this.$router.push('/moj_nalog');
                
            }
            else this.poruka_o_gresci = "Nepostojeci korisnik"
            }
        },
        created(){
          document.title = "Mystique Galirija - Logovanje"
        }
    }
</script>