<template>
    

    <div id="okruzujuci">
        <head><title>Umetnici</title></head>
        <h1 class="kulnaslov">{{$t("Umetnici")}}</h1>
        <br>
        <UmetnikComp v-for="(u, i) in umetnici" :key="i" :umetnik="u" class="umetnik_comp"></UmetnikComp>

    </div>
</template>

<style lang='less' scoped>

    @import "../globalniStilovi.less";
    .umetnik_comp{
        margin-top: 1%;
        // background-color: @bg;
    }

    #okruzujuci{
        background-color: @bg;
    }

</style>

<script>
    import UmetnikComp from '../components/UmetnikComp.vue'
    import svi_umetnici from '../data/umetnici_data'

    export default {
        name: "UmetniciShowView",
        components:{
            UmetnikComp
        },
        data(){
            return{
                umetnici: svi_umetnici
            }
        },
        created(){
            document.title = "Mystique Galirija - Umetnici"
        }
    }
</script>