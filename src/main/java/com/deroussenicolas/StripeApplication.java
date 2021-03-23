package com.deroussenicolas;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.stripe.Stripe;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Token;
import com.stripe.param.CustomerRetrieveParams;

@SpringBootApplication
public class StripeApplication implements CommandLineRunner {

	  @Value("${STRIPE_SECRET_KEY}")
	    private String stripePublicKey;
	
	public static void main(String[] args) {
		SpringApplication.run(StripeApplication.class, args);
	}

	
	
	
	@Override
	public void run(String... args) throws Exception {
		Stripe.apiKey = stripePublicKey;
		
		
		// !-- logique métier à implementer --!
		// si le customer existe (a verifier avec son adresse email dans la DB puis récuperer le champ "cus_JAWKqdezkW0kb3"
		// alors on ne crée pas de customer, on utilise celui la
		// sinon on crée un customer, on met dans la BDD le "cus_JAWKqdezkW0kb3" lié à l'adresse mail
		Customer a;
		boolean customerExists =  false;
		a = Customer.retrieve("cus_JAWKqdezkW0kb3");
		if(a != null) {
			System.out.println("exists");
			customerExists = true;
		}		
		if(!customerExists) {
			System.out.println("not exists");
			Map<String, Object> customerParameters = new HashMap<String, Object>();
			customerParameters.put("email", "aaa@aol.fr");
			a = Customer.create(customerParameters);
		}

		// !-- logique métier à implementer --!
		// on reçoit le token, amount et currency via l'API 

		/*
		Map<String, Object> cardParam = new HashMap<String, Object>();
		cardParam.put("number", "4242424242424242");
		cardParam.put("exp_month", "11");
		cardParam.put("exp_year", "2024");
		cardParam.put("cvc", "123");
		
		//on crée le token (pour l'exemple)ici mais il doit être crée dans le FRONT END
		Map<String, Object> tokenParam = new HashMap<String, Object>();
		tokenParam.put("card", cardParam);
		
		Token token = Token.create(tokenParam);
		

		Map<String, Object> source = new HashMap<String, Object>();
		source.put("source", token.getId());

		CustomerRetrieveParams retrieveParams =
				  CustomerRetrieveParams.builder()
				    .addExpand("sources")
				    .build();
		Customer customer = Customer.retrieve("cus_JAWKqdezkW0kb3", retrieveParams, null);
		customer.getSources().create(source);
		*/
		

		
		// a partir du customer id, on crée la requete bancaire
		
		String amount = "500";
		String currency = "usd";
		Map<String, Object> chargeParam = new HashMap<String, Object>();
		chargeParam.put("amount", amount);
		chargeParam.put("currency", currency);
		chargeParam.put("customer", a.getId());
		
		Charge.create(chargeParam);
		
	}

}
