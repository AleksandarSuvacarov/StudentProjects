<template>

    <div>
        <UmetnineComp :tip_umetnine="tip_umetnine" :naslov="naslov" :key="$route.fullPath"></UmetnineComp>
    </div>
    
</template>


<script>
    import UmetnineComp from '../components/UmetnineComp.vue'

    export default{
        name: 'UmetnineShowView',
        components:{ UmetnineComp },
        data(){
            return{
                tip_umetnine: "",
                naslov: ''
            }
        },
        created(){
            
            if(this.$route.path == "/slike") {
                document.title = "Mystique Galirija - Umetnine - slike"
                this.tip_umetnine = "slika",
                this.naslov = 'Slike'
            }
            else if(this.$route.path == "/skulpture") {
                document.title = "Mystique Galirija - Umetnine - skulpture"
                this.tip_umetnine = "skulptura"
                this.naslov = 'Skulpture'
            }
            else if(this.$route.path == "/ostale") {
                document.title = "Mystique Galirija - Umetnine - ostalo"
                this.tip_umetnine = "ostalo"
                this.naslov = 'Ostale umetnine'
            }
            else if(this.$route.path == "/sve_umetnine") {
                document.title = "Mystique Galirija - Umetnine"
                this.tip_umetnine = "sve"
                this.naslov = 'Umetnine'
            }
        }
    }
</script>
