const { createApp } = Vue;

createApp({
  data() {
    return {
      cliente: [],
      fechaFinal: undefined,
      filteredCardsCredit: [],
      filteredCardsDebit: []
    };
  },
  created() {
    this.loadData();
    this.verificationDate();
  },
  methods: {
    loadData: function () {
      axios.get("/api/clients/current")
          .then(response => {
              this.cliente = response.data;
              this.cliente.accounts.sort((a, b) => a.id - b.id);
              this.filteredCardsCredit = this.cliente.cards.filter(card => card.type === "CREDIT" && card.hidden == false)
              this.filteredCardsDebit = this.cliente.cards.filter(card => card.type === "DEBIT" && card.hidden == false)
          })
          .catch(err => console.log(err));
  },
  logout:function() {
    axios.post('/api/logout') 
        .then(response => {
            window.location.href = '/web/index.html';
        })
        .catch(error => {
            this.error = error.response.data.message;
        });
},
deleteCard: function(card) {
  axios.put(`/api/cards/${card.id}/hide`)
    .then(response => {
      this.loadData();
    })
    .catch(error => {
      console.log(error);
    });
},
logOut: function () {
  axios.post('/api/logout')
      .then(response => {
          window.location.href = '/web/index.html';
      })
      .catch(error => {
          this.error = error.response.data.message;
      });
    },
verificationDate() {
  let expired = new Date();
            console.log(expired)
            let options = { year: 'numeric', month: '2-digit', day: '2-digit' }
            console.log(options)
            let fechaFormateada = expired.toLocaleString('es-ES', options);
            console.log("fechaForma",fechaFormateada)
            this.fechaFinal = fechaFormateada.split("/").reverse().join("-")
            console.log("fechaFinal",this.fechaFinal)
}    
  },
}).mount("#app");