<template>
  <div class="row" id="okruzujuci">

    <div class="row"> 

        <div id="putokaz">
            <router-link class="routerLink" to='/sve_umetnine'>{{$t("Umetnine")}}</router-link>
             > 
            <router-link class="routerLink" :to="{path: '/' + zaRuter}">  {{$t(tip)}} </router-link>
             > {{$t(um.naziv)}}
        </div>

        <!-- <h3></h3> -->


        <div class="col-12">
            <div class="card">
                <div class="card-body">
                    <h2 class="card-title kulnaslov"> {{$t(um.naziv)}} </h2>
                </div>
                <img :src="'/images/' + um.slika" alt="slika" id="glavna_slika">
                
                <div class="card-body podaci">
                    <h4>{{$t("Autor_details")}}: {{$t(aut.ime)}} {{$t(aut.prezime)}}</h4>
                    <p class='card-text'> {{$t(aut.opis)}} </p>
                    <h4>{{$t("Procenjena_vrednost_details")}}: {{um.vrednost}}$</h4>
                    <h4>{{$t("Procenjena_starost_details")}}: {{um.starost}} {{$t('godina')}}</h4>

                    <hr>

                    <div id="sve_ponude">

                        <div class="ponuda" v-for="(p, index) in ponude_za_ispis" :key="index">

                            <div class="card p-3 novi_card">

                                <div class="d-flex justify-content-between align-items-center">
                                    <div class="user d-flex flex-row align-items-center">
                                        <span>
                                            <small class="font-weight-bold text-primary korisnik"> {{p.korisnik.username}} </small> 
                                            <small class="font-weight-bold komentar"> {{p.poruka}} </small>
                                        </span>   
                                    </div>
                                    <div class="user d-flex flex-row align-items-center">
                                        <span>
                                            <small class="font-weight-bold text-primary">{{p.iznos}}$</small> 
                                        </span> 
                                    </div>
                                </div>

                                <div v-if="postojiKorisnik && korisnik.username == p.korisnik.username" class="action d-flex justify-content-between mt-2 align-items-center">
                                    <div :class="{'reply':true, 'px-4': true, 'nevidljiv': korisnik != null && korisnik.username != p.korisnik.username }">
                                        <small @click="ukloni(p)">{{$t("Ukloni")}}</small>
                                    </div>
                                </div>
                                
                            </div>

                        </div>

                    </div>


                    <button v-if="postojiKorisnik" class="btn global-button" id="ostavite_ponudu" @click="ostavite_ponudu()"> {{$t('Ostavite ponudu')}} </button>

                    <div id="drzac_ponude">
                        <div class="input-group mb-3">
                            <span class="input-group-text">$</span>
                            <input type="text" class="form-control" v-model="iznos" aria-label="Amount (to the nearest dollar)">
                            <span class="input-group-text">.00</span>
                        </div>

                        <div class="input-group">
                            <span class="input-group-text"> {{$t('poruka_komentar')}} </span>
                            <textarea class="form-control" aria-label="With textarea" v-model="poruka_komentar"></textarea>
                        </div>

                        <button class="btn global-button " id="objavi" @click="objavi()"> {{$t('Objavi')}} </button>

                    </div>

                </div>
            </div>
        </div>

    </div>

    <div class="carousel-omotac text-center carousel-dark">
        <h1 class="kulnaslov">{{$t("Galerija")}}</h1>
        <br>
        <hr>
        <div id="carouselExampleControls" class="carousel slide" data-bs-ride="carousel">
        <div class="carousel-inner">
            <div v-for="(s, index) in um.galerija" :key="index" :class="{ 'carousel-item': true, 'active': index === 0}">
                <img :src="'/images/' + s" class="d-block w-100 carousel-slika" alt="..." />
            </div>
        </div>
        <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleControls" data-bs-slide="prev" >
            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
            <span class="visually-hidden">Previous</span>
        </button>
        <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleControls" data-bs-slide="next" >
            <span class="carousel-control-next-icon" aria-hidden="true"></span>
            <span class="visually-hidden">Next</span>
        </button>
        </div>
    </div>


  </div>
</template>

<style lang='less' scoped>

    @import "../globalniStilovi.less";
    /* h3 {
        text-align:start;
    } */

    .routerLink {
        //your desired design when link is clicked
        color: @fontBraon;
        // font-size: 40px;
        text-decoration: none;
        font-family: @naslovFont;
        // text-shadow: 0.5px 0.5px 0.5px @fontBraon;
        font-size: 18px;
    }


    #putokaz{
        font-size: 20px;
        text-align: left;
        margin-left: 10vw;
    }

    #okruzujuci{
        font-family: @font;
        background-color: @bg;
    }

    .card{
        margin-top: 5%;
        margin-left: 10%;
        margin-right: 10%;
        margin-bottom: 10%;
        color: @fontBraon;
        border: 3px solid @fontBraon;
    }   

    .novi_card{
        border: none;
        box-shadow: 5px 6px 6px 2px #e9ecef;
        border-radius: 4px;
        margin: 7% !important;
        border-top: solid @zelena 2px;
        border-left: solid @zelena 2px;
    }

    #glavna_slika{
        height: 80vh;
        padding: 2%;
        /* border:2px solid black; */
        object-fit: contain;
    }

    .carousel-slika {
        max-height: 80vh !important;
        max-width: 100%;
        width: auto !important;
        margin: auto;
        margin-bottom: 5%;
    }

    #ostavite_ponudu{
        margin: 1%;
    }

    #objavi{
        margin: 1%;
    }

    #drzac_ponude{
        display: none;
    }

    #sve_ponude{
        max-height: 400px;
        overflow-y: scroll;
    }

    .reply{

        margin-left: 12px;
    }

    .reply small{

        color: #b7b4b4;

    }


    .reply small:hover{

        color: green;
        cursor: pointer;

    }

    .korisnik{
        margin-right: 15px;
    }

    .nevidljiv{
        display: none;
    }



</style>


<script>
import sve_umetnine from "../data/umetnine_data.js";
import svi_umetnici from "../data/umetnici_data.js";
import $ from 'jquery';

export default {
  name: "UmetninaDetails",
  data() {
    return {
      um: {},
      aut: {},
      korisnik: {},
      ponude_korisnika: [],
      ponude_za_ispis: [],
      poruka_komentar: '',
      iznos: '',
      tip: this.$route.query.tip,
      zaRuter: '',
      postojiKorisnik: false
    };
  },
  props:[
    
  ],

  created() {
    document.title = "Mystique Galirija - Umetnine detalji"
    var uId = Number(this.$route.params.id);
    this.um = sve_umetnine.find((u) => u.id == uId);
    this.aut = svi_umetnici.find((u) => u.id == this.um.autor);

    if(localStorage.getItem('ulogovan_korisnik') != null){
        this.korisnik = JSON.parse(localStorage.getItem('ulogovan_korisnik'))
        this.postojiKorisnik = true;
    }
    else{
        this.korisnik = null;
    }

    if(localStorage.getItem('sve_ponude') != null){
        this.ponude_korisnika = JSON.parse(localStorage.getItem('sve_ponude'))

        let p = this.ponude_korisnika.filter(n => n.umetnina.id == this.um.id)

        p.sort((a, b) => new Date(a.datum_vreme).getTime() - new Date(b.datum_vreme).getTime());

        this.ponude_za_ispis = p;
    }

    var tipUmetnine = this.um.tip

    if(tipUmetnine == 'ostalo'){
        this.zaRuter = 'ostale'
    }else if(tipUmetnine == 'slika'){
        this.zaRuter = 'slike'
    }else if(tipUmetnine == 'skulptura'){
        this.zaRuter = 'skulpture'
    }


  },
  methods:{
    ostavite_ponudu(){
        $('#drzac_ponude').toggle();
    },
    objavi(){
        if(localStorage.getItem('sve_ponude') != null){
            this.ponude_korisnika = JSON.parse(localStorage.getItem('sve_ponude'))
        }

        if(this.korisnik == null || this.iznos == '') return;

        let objava = {
            korisnik: this.korisnik,
            umetnina: this.um,
            poruka: this.poruka_komentar,
            iznos: this.iznos,
            datum_vreme: new Date()
        }

        this.ponude_korisnika.push(objava);

        localStorage.setItem('sve_ponude', JSON.stringify(this.ponude_korisnika))

        // var poslednje_tri_ponude = this.ponude_korisnika.slice(-3);
        // localStorage.setItem('poslednje_tri_ponude', JSON.stringify(poslednje_tri_ponude))

        this.iznos = ''
        this.poruka_komentar = ''

        this.ponude_za_ispis.push(objava)

        this.ponude_za_ispis.sort((a, b) => new Date(a.datum_vreme).getTime() - new Date(b.datum_vreme).getTime());
        

    },
    ukloni(p){
        let index = this.ponude_korisnika.indexOf(p)
        this.ponude_korisnika.splice(index, 1)
        localStorage.setItem('sve_ponude', JSON.stringify(this.ponude_korisnika))

        var poslednje_tri_ponude = this.ponude_korisnika.slice(-3);
        localStorage.setItem('poslednje_tri_ponude', JSON.stringify(poslednje_tri_ponude))

        let po = this.ponude_korisnika.filter(n => n.umetnina.id == this.um.id)
        po.sort((a, b) => new Date(a.datum_vreme).getTime() - new Date(b.datum_vreme).getTime());
        this.ponude_za_ispis = po;

    }
  }
};
</script>