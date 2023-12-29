import Vue from "vue";
import Vuetify from "vuetify/lib";
import VueGoogleCharts from "vue-google-charts";

Vue.use(VueGoogleCharts);
Vue.use(Vuetify);

export default new Vuetify({
	theme: { dark: false },
});
