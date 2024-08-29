<template>


    <div id="okruzujuci">
        <div v-if="this.tip_umetnine !='sve'">
            <div id="putokaz">
            <router-link class="routerLink" to='/sve_umetnine'>{{$t('Umetnine')}}</router-link> > {{$t(naslov)}}
            </div>
            <!-- <h3></h3> -->
            <h1 class="kulnaslov">{{$t(naslov)}}</h1>
        </div>
        <div v-else>
            <h1 class="kulnaslov">{{$t(naslov)}}</h1>
        </div>

        



        <div class="drzac_pretrage_i_sorta row">
            <div class="input-group mb-3 drzac_pretrazi col-sm-8">
                <input type="text" class="form-control" id="pretrazi" :placeholder="$t('Pretrazi')" aria-label="Recipient's username" aria-describedby="basic-addon2">
                <div class="input-group-append">
                    <input type="checkbox" class="btn-check " id="naziv_search">
                    <label id="naziv_dugme" class="btn ivicadugme" for="naziv_search">{{$t("Naziv_sort")}}</label><br>
                </div>
                <div class="input-group-append">
                    <input type="checkbox" class="btn-check" id="umetnik_search">
                    <label id="umetnik_dugme" class="btn ivicadugme" for="umetnik_search">{{$t("Umetnik_sort")}}</label><br>
                </div>
            </div>

            <div class="dropdown col-sm-4">
                <button class="btn global-button dropdown-toggle" type="button" id="dropdownMenuButton1" data-bs-toggle="dropdown" aria-expanded="false">
                    {{$t("Sortiraj")}}

                </button>
                <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton1">
                    <li><a class="dropdown-item" href="#" @click="sortiraj('naziv')">{{$t("Naziv_sort")}}</a></li>
                    <li><a class="dropdown-item" href="#" @click="sortiraj('umetnik')">{{$t("Umetnik_sort")}}</a></li>
                    <li><a class="dropdown-item" href="#" @click="sortiraj('oba')">{{$t("Umetnik_i_naziv_sort")}}</a></li>
                </ul>
            </div>

        </div>
        

        <div class="row"> 
            <div class="col-md-4 col-sm-12" v-for="mu in moje_umetnine" :key="mu.umetnina.id">
                <div class="card">
                    <img :src="'/images/' + mu.umetnina.slika" alt="slika">
                    <div class="card-body">
                        <h4 class="card-title"> {{$t(mu.umetnina.naziv)}} </h4>
                        <p class="card-text"> {{$t(mu.umetnik.ime)}}   {{$t(mu.umetnik.prezime)}} </p>
                        <a href="#" class="btn global-button" @click="pogledaj(mu.umetnina.id)">{{$t("Pogledaj")}}</a>

                    </div>
                </div>
            </div>
        </div>


    </div>
    
</template>

<style lang='less' scoped>

    @import "../globalniStilovi.less";

    .routerLink {
        //your desired design when link is clicked
        color: @fontBraon;
        // font-size: 40px;
        text-decoration: none;
        font-family: @naslovFont;
        // text-shadow: 0.5px 0.5px 0.5px @fontBraon;
        font-size: 18px;
    }

    .ivicadugme{
        border: 1px solid @fontBraon;
    }

    .dugmeboja{
        background-color: @fontBraon;
        color: @bg;
    }


    #putokaz{
        font-size: 20px;
        text-align: left;
        margin-left: 10vw;
    }

    #okruzujuci{
        font-family: @font;
        background-color: @bg;
        color: @fontBraon;
    }
    
    .card{
        margin: 4%;
        color: @fontBraon;
        border: 3px solid @fontBraon;
        
    }   

    img{
        height: 50vh;
        padding: 2%;
        /* border:2px solid black; */
        object-fit: contain;
    }

    .drzac_pretrazi{
        width: 50%;
        margin: auto;
    }



</style>


<script>
import $ from 'jquery';

import sve_umetnine from '../data/umetnine_data.js'
import svi_umetnici from '../data/umetnici_data.js'

export default{
    name: 'UmetnineComp',
    data(){
        return{
            moje_umetnine: [],
            zaRuter: ''
        }
    },
    props:[
        'tip_umetnine',
        'naslov'
    ],
    methods:{
        pogledaj(umetnina_id){
            var zaSlanje;
            var poslati;
            if(this.tip_umetnine == 'sve'){
                zaSlanje = sve_umetnine.find(u=>u.id == umetnina_id).tip        
                if(zaSlanje == 'slika'){
                    poslati = 'Slike'
                }else if(zaSlanje == 'skulptura'){
                    poslati = 'Skulpture'
                }else if(zaSlanje == 'ostalo'){
                    poslati = 'Ostale umetnine'
                }
            }else{
                zaSlanje = this.naslov
                poslati = zaSlanje
            }
            // let novaPutanja = '/umetnina/' + umetnina_id;
            // this.$router.push(novaPutanja);
            this.$router.push({
                name: 'UmetninaView', 
                params:{id: umetnina_id},
                query: {
                    
                    //hlebic: this.$t('Umetnine') + " > " + this.naslov,
                    tip: poslati,

                }
            });

        },
        ucitaj_moje_umetnine(){
            this.moje_umetnine = []
            var umetninice;
            if(this.tip_umetnine != 'sve'){
                umetninice = sve_umetnine.filter(u => u.tip == this.tip_umetnine);
            }else{
                umetninice = sve_umetnine.filter(u => u.id > -1);
            }
                

            umetninice.forEach(s => {
                let u = svi_umetnici.find(umetnik => umetnik.id == s.autor)
                this.moje_umetnine.push({
                    umetnina: s,
                    umetnik: u
                })

            });
        },
        sortiraj(tip){
            this.ucitaj_moje_umetnine()
            $("#pretrazi").val("")
            $("#naziv_search").prop("checked", false)
            $("#umetnik_search").prop("checked", false)

            if(tip == 'naziv'){
                this.moje_umetnine.sort((a, b) => {
                    let a_naziv = this.$t(a.umetnina.naziv)
                    let b_naziv = this.$t(b.umetnina.naziv)
                    if(a_naziv > b_naziv) return 1
                    else if(a_naziv == b_naziv) return 0
                    else return -1;
                    })
            }
            else if(tip == 'umetnik'){
                this.moje_umetnine.sort((a, b) => {
                    let a_naziv = this.$t(a.umetnik.ime) + " " + this.$t(a.umetnik.prezime)
                    let b_naziv = this.$t(b.umetnik.ime) + " " + this.$t(b.umetnik.prezime)
                    if(a_naziv > b_naziv) return 1
                    else if(a_naziv == b_naziv) return 0
                    else return -1;
                    })
            }
            else if(tip == 'oba'){
                this.moje_umetnine.sort((a, b) => {
                    let a_naziv = this.$t(a.umetnik.ime) + " " + this.$t(a.umetnik.prezime) + " " + this.$t(a.umetnina.naziv)
                    let b_naziv = this.$t(b.umetnik.ime) + " " + this.$t(b.umetnik.prezime) + " " + this.$t(b.umetnina.naziv)
                    if(a_naziv > b_naziv) return 1
                    else if(a_naziv == b_naziv) return 0
                    else return -1;
                    })
            }




        }
    },
    created(){
        this.ucitaj_moje_umetnine();
        
    },
    mounted(){
        const self = this;
        $(document).ready(() => {


           
            function osvezi_pretragu(){
                var value = $(this).val().toLowerCase();
                var naziv_search = $('#naziv_search').is(':checked');
                var umetnik_search = $('#umetnik_search').is(':checked');
                // if ($(this).length === 0 || !naziv_search && !umetnik_search) return;
                if ($(this).length === 0)return;
                self.ucitaj_moje_umetnine();
                if(!naziv_search && !umetnik_search) return;
                let filtrirano = []
                if(naziv_search && !umetnik_search)
                    filtrirano = self.moje_umetnine.filter(u => self.$t(u.umetnina.naziv).toLowerCase().indexOf(value) > -1)
                else if(!naziv_search && umetnik_search)
                    filtrirano = self.moje_umetnine.filter(u => self.$t(u.umetnik.ime).toLowerCase().indexOf(value) > -1 || self.$t(u.umetnik.prezime).toLowerCase().indexOf(value) > -1)
                else
                    filtrirano = self.moje_umetnine.filter(u => self.$t(u.umetnina.naziv).toLowerCase().indexOf(value) > -1 || self.$t(u.umetnik.ime).toLowerCase().indexOf(value) > -1 || self.$t(u.umetnik.prezime).toLowerCase().indexOf(value) > -1)
                self.moje_umetnine = filtrirano
            }

            $("#pretrazi").on("keyup", osvezi_pretragu);
            // $("#naziv_search").on("change", osvezi_pretragu);
            // $("#umetnik_search").on("change", osvezi_pretragu);


            $("#naziv_search").on('change', function () {
                // $('#naziv_search').addClass('dugmeboja')
                var naziv_search = $('#naziv_search').is(':checked');
                if(naziv_search){
                    // $('#naziv_dugme').addClass('dugmeboja');
                    $('#naziv_dugme').css({
                        "background-color" : "#4d2600",
                        "color": "#fef9f2"
                    })
                }else{
                    $('#naziv_dugme').css({
                        "background-color" : "#fef9f2",
                        "color": "#4d2600"
                    })
                }
            });


            $("#umetnik_search").on('change', function () {
                // $('#naziv_search').addClass('dugmeboja')
                var umetnik_search = $('#umetnik_search').is(':checked');
                if(umetnik_search){
                    // $('#naziv_dugme').addClass('dugmeboja');
                    $('#umetnik_dugme').css({
                        "background-color" : "#4d2600",
                        "color": "#fef9f2"
                    })
                }else{
                    $('#umetnik_dugme').css({
                        "background-color" : "#fef9f2",
                        "color": "#4d2600"
                    })
                }
            });



        });
    }

}
</script>
