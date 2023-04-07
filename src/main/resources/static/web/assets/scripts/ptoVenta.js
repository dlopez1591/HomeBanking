const { createApp } = Vue;

createApp({
  data() {
    return {
     amount:undefined,
     number: undefined,
     cvv: undefined,
     description: undefined
    };
  },
  created() {
    
  },
  methods: {
    createTransaction(){
        axios.post('/api/payment',{
            number: this.number,
            cvv: this.cvv,
            amount: this.amount,
            description: this.description
        })
        .then(response =>{ 
            console.log()
    })
    .catch(error =>{
        console.log(error)
    });
    }
  },
}).mount("#app");