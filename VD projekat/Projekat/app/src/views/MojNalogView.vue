<template>
    <div id='okruzujuci'>
        <!-- Moj nalog {{korisnik.username}} -->


            <div class="container">
        <div class="row">
            <div class="col-md-3">
                <div class="osahan-account-page-left shadow-sm bg-white h-100">
                    <div class="border-bottom p-4">
                        <div class="osahan-user text-center">
                            <div class="osahan-user-media">
                                <img class="mb-3 rounded-pill shadow-sm mt-1" :src="/images/ + korisnik.picture" alt="slika">
                                <div class="osahan-user-media-body">
                                    <h6 class="mb-2"> {{korisnik.firstname}} {{korisnik.lastname}} </h6>
                                    <p class="mb-1">  {{korisnik.number}} </p>
                                    <p> {{korisnik.mail}} </p>
                                </div>
                            </div>
                        </div>
                    </div>
                    <ul class="nav nav-tabs flex-column border-0 pt-4 pl-4 pb-4" id="myTab" role="tablist">
                        <li class="nav-item">
                            <a class="nav-link" id="favourites-tab" data-toggle="tab" href="#favourites" role="tab" aria-controls="favourites" aria-selected="false">
                                <i class="icofont-heart"></i> {{$t(korisnik.status)}} 
                            </a>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="col-md-9">
                <div class="osahan-account-page-right shadow-sm bg-white p-4 h-100">
                    <div class="tab-content" id="myTabContent">
                        <div class="tab-pane fade  active show" id="favourites" role="tabpanel" aria-labelledby="favourites-tab">

                            <h4 class="font-weight-bold mt-0 mb-4 kulnaslov"> {{$t('Moje ponude')}} </h4>

                            <div class="row">
                                <div class="col-md-4 col-sm-6 mb-4 pb-2" v-for="(mp, i) in moje_ponude" :key="i">
                                    <div class="list-card bg-white h-100 rounded overflow-hidden position-relative shadow-sm">
                                        <div class="list-card-image">
                                            <a href="#" @click="predji(mp.umetnina)">
                                                <img :src="/images/ + mp.umetnina.slika" class="img-fluid item-img">
                                            </a>
                                        </div>
                                        <div class="p-3 position-relative">
                                            <div class="list-card-body">
                                                <h5 class="mb-1">
                                                    <a href="#" class="text-black" @click="predji(mp.umetnina)">
                                                        {{$t(mp.umetnina.naziv)}}
                                                    </a>
                                                </h5>
                                                <p class="text-gray mb-3">
                                                    {{mp.iznos}} $
                                                </p>
                                            </div>
                                            <div class="list-card-badge">
                                                <!-- <span class="badge badge-danger"></span> -->
                                                 <small> 
                                                    <button @click="ukloni(mp)">{{$t('Ukloni')}}</button> 
                                                </small>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                
                                <!-- <div class="col-md-12 text-center load-more">
                                    <button class="btn btn-primary" type="button" disabled="">
                                        <span class="spinner-grow spinner-grow-sm" role="status" aria-hidden="true"></span> Loading...
                                    </button>
                                </div> -->

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

        



    </div>
</template>


<style lang="less" scoped>

    @import "../globalniStilovi.less";

    #okruzujuci{
        font-family: @font;
        background-color: @bg;
    }


    body{
    margin-top:20px;
    background:#eee;
    }
    /* My Account */
    .payments-item img.mr-3 {
        width: 47px;
    }
    .order-list .btn {
        border-radius: 2px;
        min-width: 121px;
        font-size: 13px;
        padding: 7px 0 7px 0;
    }
    .osahan-account-page-left .nav-link {
        padding: 18px 20px;
        border: none;
        font-weight: 600;
        color: #535665;
    }
    .osahan-account-page-left .nav-link i {
        width: 28px;
        height: 28px;
        background: #535665;
        display: inline-block;
        text-align: center;
        line-height: 29px;
        font-size: 15px;
        border-radius: 50px;
        margin: 0 7px 0 0px;
        color: #fff;
    }
    .osahan-account-page-left .nav-link.active {
        background: #f3f7f8;
        color: #282c3f !important;
    }
    .osahan-account-page-left .nav-link.active i {
        background: #282c3f !important;
    }
    .osahan-user-media img {
        width: 90px;
    }
    .card offer-card h5.card-title {
        border: 2px dotted #000;
    }
    .card.offer-card h5 {
        border: 1px dotted #daceb7;
        display: inline-table;
        color: #17a2b8;
        margin: 0 0 19px 0;
        font-size: 15px;
        padding: 6px 10px 6px 6px;
        border-radius: 2px;
        background: #fffae6;
        position: relative;
    }
    .card.offer-card h5 img {
        height: 22px;
        object-fit: cover;
        width: 22px;
        margin: 0 8px 0 0;
        border-radius: 2px;
    }
    .card.offer-card h5:after {
        border-left: 4px solid transparent;
        border-right: 4px solid transparent;
        border-bottom: 4px solid #daceb7;
        content: "";
        left: 30px;
        position: absolute;
        bottom: 0;
    }
    .card.offer-card h5:before {
        border-left: 4px solid transparent;
        border-right: 4px solid transparent;
        border-top: 4px solid #daceb7;
        content: "";
        left: 30px;
        position: absolute;
        top: 0;
    }
    .payments-item .media {
        align-items: center;
    }
    .payments-item .media img {
        margin: 0 40px 0 11px !important;
    }
    .reviews-members .media .mr-3 {
        width: 56px;
        height: 56px;
        object-fit: cover;
    }
    .order-list img.mr-4 {
        width: 70px;
        height: 70px;
        object-fit: cover;
        box-shadow: 0 .125rem .25rem rgba(0, 0, 0, .075)!important;
        border-radius: 2px;
    }
    .osahan-cart-item p.text-gray.float-right {
        margin: 3px 0 0 0;
        font-size: 12px;
    }
    .osahan-cart-item .food-item {
        vertical-align: bottom;
    }

    .h1, .h2, .h3, .h4, .h5, .h6, h1, h2, h3, h4, h5, h6 {
        color: #000000;
    }

    .shadow-sm {
        box-shadow: 0 .125rem .25rem rgba(0,0,0,.075)!important;
    }

    .rounded-pill {
        border-radius: 50rem!important;
    }
    a:hover{
        text-decoration:none;
    }

    .item-img {
        width: 60%;
        height: 14vh;
        margin: auto;
        object-fit: cover;
    }

    // .list-card{
    //     border-top: solid @zelena;
    // }

    // Dugme obrisi

    button {
        position: relative;
        font-size: 1em;
        padding: 0.7em 1.4em;
        background-color: #BF0426;
        text-decoration: none;
        border: none;
        border-radius: 0.5em;
        color: #DEDEDE;
        box-shadow: 0.25em 0.25em 0.25em rgb(254, 206, 206);
    }

    button::before {
        position: absolute;
        content: '';
        height: 0;
        width: 0;
        top: 0;
        left: 0;
        background: linear-gradient(135deg, rgb(255, 255, 255) 0%, rgb(255, 255, 255) 50%, rgba(150,4,31,1) 50%, rgba(191,4,38,1) 60%);
        border-radius: 0 0 0.5em 0;
        box-shadow: 0.2em 0.2em 0.2em rgba(0, 0, 0, 0.3);
        transition: 0.3s;
    }

    button:hover::before {
        width: 1.6em;
        height: 1.6em;
    }

    button:active {
        box-shadow: 0.2em 0.2em 0.3em rgb(255, 255, 255, 0.3);
        transform: translate(0.1em, 0.1em);
    }

</style>


<script>

export default {
    name: 'MojNalogView',
    data(){
        return{
            korisnik: {},
            moje_ponude: [],
            sve_ponude: []
        }
    },
    created(){
        document.title = "Mystique Galirija - Moj nalog"
        this.korisnik = JSON.parse(localStorage.getItem('ulogovan_korisnik'))
        this.sve_ponude = JSON.parse(localStorage.getItem('sve_ponude'))
        if(this.sve_ponude != null){
            this.moje_ponude = this.sve_ponude.filter(p => p.korisnik.username == this.korisnik.username)
        }
        else{
            this.sve_ponude = []
        }
    },
    methods:{
        ukloni(mp){
            let index = this.sve_ponude.indexOf(mp)
            this.sve_ponude.splice(index, 1)
            localStorage.setItem('sve_ponude', JSON.stringify(this.sve_ponude))

            var poslednje_tri_ponude = this.sve_ponude.slice(-3);
            localStorage.setItem('poslednje_tri_ponude', JSON.stringify(poslednje_tri_ponude))


            this.moje_ponude = this.sve_ponude.filter(p => p.korisnik.username == this.korisnik.username)
        },
        predji(u){

            let naslov;
            switch (u.tip) {
                case 'slika':
                    naslov = "Slike"
                    break;
                case 'skulptura':
                    naslov = "Skulpture"
                    break;
                case 'ostalo':
                    naslov = "Ostale umetnine"
                    break;
            }
            
            this.$router.push({
                name: 'UmetninaView',
                params: { id: u.id },
                query: { tip: naslov}
            });

        }
    }

}
</script>