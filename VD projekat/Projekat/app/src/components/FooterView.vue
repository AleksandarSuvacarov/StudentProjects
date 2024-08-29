<template>
    <div class="row">
        <div class="pomocni">
            nesto
        </div>
        <div class="footer containter col-xl-12">
            Copyright 2023, Jovana Simić i Aleksandar Suvačarov, Odsek za softversko inženjerstvo Elektrotehničkog fakulteta Univerziteta u Beogradu
        </div>
    </div>
</template>

<style lang='less' scoped>

    @import "../globalniStilovi.less";

    .pomocni{
        height: 30vh;
        background-color: @bg;
        color: @bg;
    }

    .footer{
        background-color: @bg;
        font-size: 22px;
        font-family: @font;
        // font-weight: bold;
        // position: fixed;
        left: 0;
        bottom: 0;
        width: 100%;
        height: 5vh;
        color: @fontBraon;
        text-align: center;
        border-top: 1px solid @fontBraon;

    }
</style>

<script>
export default {
    name: 'FooterView',
}
</script>