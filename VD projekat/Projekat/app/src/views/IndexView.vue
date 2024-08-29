<template>

    
    <div class="okruzujuci container-fluid">

        <div class="home row" :class="smallScreen=='true'? 'spec': 'notSpec'">

        
    
            

            <div class="levi_deo col-12 col-md-5 col-lg-5 " >
                <img :src="src" alt="slika" id="slika">
            </div>

            <div class="desni_deo col-12 col-md-7 col-lg-7 " >
                <h3 class="kulnaslov">Mystique {{$t("Galerija")}} </h3>
                <div class="text">
                    {{$t("opis_pocetna")}}
                    
                    <br><br>
                    {{$t("cekanj_pocetna")}}
                    
                </div>
            

            </div>

        </div>

        <div class="home2 row" :class="smallScreen=='true'? 'spec2': 'notSpec2'">

        
            <div class="levi_deo2 col-12 col-md-5 col-lg-5 " >
                <h3 class="kulnaslov"> {{$t("Aktuelnosti")}} </h3>
                <div class="text2">
                    {{$t("nova_slika")}}
                    
                    
                    
                </div>

                
            </div>

            <div class="desni_deo2 col-12 col-md-7 col-lg-7 " >
                
                <img src="/images/deveta_simfonija.jpg" alt="slika" id="slika2">
            

            </div>

        </div>



        <div id="ponude" class="row">
            <h3 id="najpon" class="kulnaslov" >{{$t("nova_ponuda_naslov")}}</h3>
            <div v-for="p of ponudePoslednje" :key="p.datum_vreme" class="ponuda col-lg-4 col-md-4 col-sm-12" >
                <div :class="smallScreen=='true'? 'unutraSpec': 'unutra'">
                    {{$t("nova_ponuda_korisnik")}}: {{p.korisnik.firstname}}
                    <br>
                    {{$t("nova_ponuda_um")}}: {{$t(p.umetnina.naziv)}}
                    <br>
                    {{$t("nova_ponuda_vr")}}: {{p.iznos}} $
                </div>
            </div>
            <!-- <div class="ponuda col-lg-3 col-md-3 col-sm-12">
                <div class="unutra">
                    nesto
                </div>
            </div>
            <div class="ponuda col-lg-3 col-md-3 col-sm-12">
                <div class="unutra">
                    nesto
                </div>
            </div> -->


        </div>

    </div>
</template>


<style lang='less' scoped>

    @import "../globalniStilovi.less";

    .unutra{
        background-color: @ponudePoz;
        width: 20vw;
        text-align: left;
        padding: 2vw;
        border-radius: 20px;
        border: 2px solid @fontBraon;
        font-size: 20px;
        color: @fontBraon;
    }

    .unutraSpec{
        background-color: @ponudePoz;
        width: 50vw;
        text-align: left;
        padding: 2vw;
        border-radius: 20px;
        border: 2px solid @fontBraon;
        font-size: 20px;
        color: @fontBraon;
        margin-top: 3vh;
    }

    #najpon{
        margin-bottom: 50px;
    }

    .spec{
        background-color: @fontBraon !important; 
    }

    .notSpec{
        height: 60vh;
        background: linear-gradient(to left, @fontBraon 70%, #fef9f2 40%) !important;
    }

    .spec2{
        background-color: @fontBraon !important; 
    }

    .notSpec2{
        height: 60vh;
        background: linear-gradient(to right, @fontBraon 65%, #fef9f2 40%) !important;
    }

    .okruzujuci{
        font-family: @font;
        color: @fontBraon;
        background-color: @bg;
    }

    #slika{
        max-width: 90%;
        max-height: 50vh;
        object-fit: contain;
        border-radius: 15px;
        margin-left: 5vw;
    }
    #slika2{
        max-width: 90%;
        max-height: 50vh;
        object-fit: contain;
        border-radius: 15px;
        // margin-left: 5vw;
        margin-right: 5vw;
    }

    #ponude{
        // background-color: green;
        margin-top: 8vh;
        display: flex;
        align-items: center;
        justify-content: center;
        
    }

    .ponuda{
        // background-color: aqua;
        display: flex;
        align-items: center;
        justify-content: center;
    }

    .home{
        
        font-family: @font;
    }
    .home2{
        
        font-family: @font;
        margin-top: 100px;
    }
        

    .levi_deo{
        // background-color: aquamarine;
        // margin-left: 5vw;
        
        display: flex;
        align-items: center;
        justify-content: center;

    }
   

    .desni_deo{

        color: @bg;
        font-size: 30px;
        text-align: left;

    }

    .levi_deo2{
        // background-color: aquamarine;
        // margin-left: 5vw;
        
        color: @bg;
        font-size: 30px;
        text-align: left;

    }
   

    .desni_deo2{

        display: flex;
        align-items: center;
        justify-content: center;

        

    }

    .desni_deo > h3, .text{
        margin-left: 5vw;
        margin-top: 5vh;
    }

    .levi_deo2 > h3{
        margin-left: 10vw;
        margin-top: 5vh;
    }
    .levi_deo2 > div{
        margin-left: 10vw;
        // margin-top: 5vh;
    }

    .text{
        font-size: 20px;
        margin-right: 12vw;
    }
    .text2{
        font-size: 20px;
        margin-top: 5vh;
        // margin-right: 12vw;
    }

    

</style>

<script>
import $ from 'jquery';


export default {
  
  
    name: 'IndexView',
    data(){
        return{
            username: '',
            password: '',
            poruka_o_gresci: '',
            // toSrc:'/images/tajna_vecera.jpg',
            currSlika: 0,
            slike : ['/images/tajna_vecera.jpg', '/images/gatamelata.jpg','/images/zvezdana_noc.jpg'],
            smallScreen: 'false',
            ponudePoslednje:{}
        }
    },
    components: {
    },
    created(){
        document.title = "Mystique Galirija - Početna"
        var user = localStorage.getItem('ulogovan');
        if( user == null){
        $('#ulogujse').show();
        $('#izlogujse').hide();
        $('#mojnalog').hide();
        }else if(user == 'false'){
        $('#ulogujse').show();
        $('#izlogujse').hide();
        $('#mojnalog').hide();
        }else if(user == 'true'){
        $('#ulogujse').hide();
        $('#izlogujse').show();
        $('#mojnalog').show();
        }
        // if(localStorage.getItem('poslednje_tri_ponude') != null){
        //     this.ponudePoslednje = JSON.parse(localStorage.getItem('poslednje_tri_ponude'));
        // }
        this.ponudePoslednje = JSON.parse(localStorage.getItem('sve_ponude'))
        if(this.ponudePoslednje == null) this.ponudePoslednje = []
        else{
            let p = this.ponudePoslednje
            p.sort((a, b) => new Date(b.datum_vreme).getTime() - new Date(a.datum_vreme).getTime());
            this.ponudePoslednje = []
            for(let i = 0; i < p.length && i < 3; i++){
                this.ponudePoslednje.push(p[i])
            }
        }
    },
    methods: {
        callFunction(){
           
            setInterval(() => {
                if(this.currSlika === 2){
                    this.currSlika = 0;
                }else{
                    this.currSlika += 1;
                }
                // this.currSlika = this.currSlika === this.slike.length - 1 ? 0 : this.currSlika + 1;
            }, 5000);
        }
    },
    
    computed:{
        src: function(){
            return this.slike[this.currSlika]
        },
        
    },
    mounted(){
        $(document).ready(() => {
            this.callFunction();

            $(window).resize(() => {
                this.smallScreen = window.innerWidth < 1200? 'true':'false';
                console.log('isSmallScreen:', this.smallScreen);
            });
        });
    }
    // mounted(){
    //     $(document).ready(() => {
    //         var slike =  ['/images/tajna_vecera.jpg', '/images/gatamelata.jpg','/images/mika_slika.jpg']
    //         var i = 1;
    //         setInterval(function () {
    //             if(i == 2){
    //                 i = 0;
    //             }else{
    //                 i++;
    //             }
    //             this.toSrc = slike[2]
    //         }, 5000);

    //     });
    // }
}
</script>
