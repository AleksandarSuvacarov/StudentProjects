<template>
    <div>

        <nav class="navbar navbar-expand-lg" id='navMoj'>
            <!-- <span class="me-auto">                
                <img src="../assets/logo.jpg" alt="Logo" class='logo'>
            </span> -->
            <div class="container-fluid">
                <a v-if="$i18n.locale == 'srb'" class="navbar-brand" href="#">                
                    <img src="../assets/logo2.jpg" alt="Logo" class='logo'>
                </a>
                <a v-else class="navbar-brand" href="#">                
                    <img src="../assets/logo3.jpg" alt="Logo" class='logo'>
                </a>
                <!-- <a class="navbar-brand" href="#">Pocetna</a> -->
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation" >
                <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse ms-auto" id="navbarNavDropdown">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item">
                        <router-link to='/'> <a :class="{ 'active': $route.path === '/', 'nav-link': true }" href="#">{{$t("Pocetna")}}<span class="sr-only"></span></a> </router-link> 
                    </li>
                    <li class="nav-item dropdown" id="menu-products">
                        
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" data-bs-toggle="dropdown" @click="predji_na_sve_umetnine()">
                            {{$t("Umetnine")}}
                        </a>
                        
                        <ul class="dropdown-menu ms-auto" id="dropdown-products">
                            <li> <router-link to='/slike'> <a class="dropdown-item" href="#">{{$t("Slike")}}</a> </router-link> </li>
                            <li> <router-link to='/skulpture'> <a class="dropdown-item" href="#">{{$t("Skulpture")}}</a> </router-link> </li>
                            <li> <router-link to='/ostale'> <a class="dropdown-item" href="#">{{$t("Ostale umetnine")}}</a> </router-link> </li>
                        </ul>
                    </li>
                    <li class="nav-item">
                        <router-link to='/umetnici'> <a :class="{ 'active': $route.path === '/umetnici', 'nav-link': true }" href="#">{{$t("Umetnici")}}</a> </router-link>
                    </li>

                    <li class="nav-item" id="mojnalog">
                        <router-link to='/moj_nalog'> <a class="nav-link" href="#">{{$t("Moj nalog")}}</a> </router-link>
                    </li>

                    <li class="nav-item" id="ulogujse">
                        <router-link to='/ulogujse'><a class="nav-link" href="#">{{$t("ulogujse")}}</a></router-link> 
                    </li>


                  
                    <li class="nav-item">
                        <router-link to='/o_nama'><a class="nav-link" href="#">{{$t("O nama")}}</a></router-link>
                    </li>
                    <li class="nav-item" id="izlogujse">
                        <a class="nav-link"  @click="izlogujse()"  href="#">{{$t("izlogujse")}}
                        <!-- <img src="../assets/logout.jpg" alt="logout" class='logout'> -->
                        </a>
                    </li>
                    <li class="nav-item">
                        <a href="#" @click="prevedi()"><img id='slika_prevod' src="../assets/srb_en.png" alt="..."></a>
                        <input type="text" v-model="$i18n.locale" id="sakriven" style="display:none">
                    </li>
                    <!-- <li v-if="ulogovan==true" class="nav-item">
                        <router-link to='/'>
                        <a class="nav-link" href="#">Izloguj se</a>
                        
                        </router-link> 
                    </li> -->
                    
                    


                     <!-- <div class="locale-changer">
                        <select v-model="$i18n.locale">
                        <option v-for="(lang, i) in $i18n.availableLocales" :key="`Lang${i}`" :value="lang">
                            {{ lang }}
                        </option>
                        </select>
                    </div> -->

                </ul>
                </div>
            </div>
            
        </nav>


    </div>

</template>

<style lang='less' scoped>

    @import "../globalniStilovi.less";

    .routerClass{
        color: @fontBraon;
    }

    #dropdown-products {
        display: none;
    }

    #menu-products:hover #dropdown-products {
        display: block;
    }

    .logout{
        width: 10vh;
    }

    .logo{
        width: 20vh;
        text-align: left;
    }

    .navbar-nav{
        margin-right: 5%;
    }

    .navbar-toggler{
        margin-right: 5%;
    }

    .crna_slova{
        color: black;
    }

    #slika_prevod{
        width: 30px;
        margin-left: 2%;
    }

    #navMoj{
        background-color: @bg ;
        color: @fontBraon ;
        font-size: 20px;
        font-weight: bold;
    }

    #izlogujse{
        margin-right: 20px;
    }


</style>

<script>
// import eventBus from '../data/eventBus';
import $ from 'jquery';

export default {
  name: "HeaderComp",
  data(){
    return {
        ulogovan: false
    }
  },
  methods:{
    prevedi(){
        let jezik = this.$i18n.locale
        if(jezik == 'srb') this.$i18n.locale = 'en'
        else this.$i18n.locale = 'srb'

        //this.$forceUpdate();
        //let putanja = this.$route.path
        //this.$router.push('/')
        //location.reload()
        //this.$emit('languageChange');
    },
    izlogujse(){
        localStorage.removeItem('ulogovan');
        localStorage.removeItem('ulogovan_korisnik');
        this.ulogovan = false;
        $('#ulogujse').show();
        $('#izlogujse').hide();
        $('#mojnalog').hide();
        this.$router.push('/');
    },
    refresh(){
        this.$forceUpdate();
    },
    predji_na_sve_umetnine(){
        this.$router.push('/sve_umetnine');
    }
  },
    created() {
        // eventBus.$on('refresh-header', this.refresh);

        if (localStorage.getItem("ulogovan") == null || localStorage.getItem("ulogovan") == false) {
            this.ulogovan = false;
            localStorage.setItem('ulogovan', false);
        } else {
            this.ulogovan = true
        }
        }
};
</script>