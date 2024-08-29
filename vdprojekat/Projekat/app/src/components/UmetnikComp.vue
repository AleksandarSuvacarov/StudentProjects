<template>
    <div>

        <div class="objava row">

            <span class="col-xl-2 objava_left">
            <img :src="'/images/' + umetnik.slika" alt="slika">
            </span>

            <span class="col-md-8">
                <h1 class="kulnaslov"> {{$t(umetnik.ime)}} {{$t(umetnik.prezime)}} </h1>

                <p>{{$t(umetnik.opis)}}</p>

            </span>

            <span class="col-md-2 drzac_dugmeta" style="display: grid;">
                <button class="btn global-button dugme_prijava" @click="pregled()">{{$t("Pregled dela")}}</button>               
            </span>

        </div>


        <div class="zastita" style="display:none">
            <div :id="'za_print' + umetnik.id" class="za_print">
                <h3>{{$t(umetnik.ime)}} {{$t(umetnik.prezime)}}</h3>
                <ul>
                    <li v-for="(u, i) in umetnine" :key="i"> {{$t(u.naziv)}} </li>
                </ul>                
            </div>
        </div>



    </div>
</template>

<style lang='less' scoped>

    @import "../globalniStilovi.less";



    .objava{
        text-align: left;
        margin:auto;
        padding: 8px;
        border: 2px solid @fontBraon;
        background-color: @bg;
        border-radius: 10px;
        
        width: 80%;
    }

    .objava img{
        width: 120px;
        height: 120px;
        border:2px solid black;
        border-radius: 8px;
        object-fit: cover;
    }

    .dugme_prijava{
        margin: auto;
    }

    .za_print{
        width: 500px;
        margin: auto;
        /* word-wrap: break-word; */
        /* overflow-wrap: break-word;  */
        background-color: #f2f2f2;
        border: 1px solid #ccc;
        padding: 10px;
        border-radius: 5px;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); 
        hyphens: auto;
    }

    .za_print h3 {
        margin: 0 0 10px;
        font-size: 22px;
        font-weight: bold;
    }

    .za_print ul {
        list-style-type: none;
        padding: 0;
        margin: 0;
    }

    .za_print li {
        margin-bottom: 5px;
        font-size: 18px;
        font-family: 'Courier New', Courier, monospace;
        font-style: italic;
        /* font-weight: bold; */
    }

    .za_print li:before {
        content: "•";
        margin-right: 5px;
        color: #666;
    }

    

</style>

<script>
    //import jsPDF from "jsPDF" 
    import jsPDF from 'jspdf/dist/jspdf.es.min'
    import sve_umetnine from '../data/umetnine_data.js'
    //import UmetniciShowViewVue from '../views/UmetniciShowView.vue'


    export default {
        name: "UmetnikComp",
        props:[
            "umetnik"
        ],
        data(){
            return{
                umetnine: [],
                naziv_pdf: ''
            }
        },
        created(){
            
        },
        methods:{
            pregled(){
                const doc = new jsPDF({ unit: 'pt' }) // create jsPDF object
                const pdfElement = document.getElementById('za_print' + this.umetnik.id) // HTML element to be converted to PDF
                this.umetnine = sve_umetnine.filter(u => u.autor == this.umetnik.id)
                this.naziv_pdf = this.$t(this.umetnik.ime) + this.$t(this.umetnik.prezime) + ".pdf"
                this.naziv_pdf = this.naziv_pdf.replace(/\s/g, "");
                doc.html(pdfElement, {
                    callback: (pdf) => {
                    pdf.save(this.naziv_pdf)
                    },
                    margin: 32, // optional: page margin
                    // optional: other HTMLOptions
                })
                
            }
        }
    }
</script>