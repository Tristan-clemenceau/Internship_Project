$(document).ready(function(){
	  /*ALERT*/
      $alert_recapitulatif = $("#alert_recapitulatif");
      $alert_recapitulatif_message = $("#alert_recapitulatif_message");
      /*BOUTTON*/
      $btn_ticket_enfant_moins = $("#btn_ticket_enfant_moins");
      $btn_ticket_enfant_plus = $("#btn_ticket_enfant_plus");
      $btn_ticket_adulte_moins = $("#btn_ticket_adulte_moins");
      $btn_ticket_adulte_plus = $("#btn_ticket_adulte_plus");
      $btn_ticket_persoa_moins = $("#btn_ticket_persoa_moins");
      $btn_ticket_persoa_plus = $("#btn_ticket_persoa_plus");
      $btn_recapitulatif_annuler = $("#btn_recapitulatif_annuler");
      $btn_ticket_persoa_confirmer = $("#btn_recapitulatif_confirmer");

      /*AFFICHAGE VALEUR*/
      $lbl_ticket_enfant = $("#recapitulatif_ticket_Enfant");
      $lbl_ticket_adulte = $("#recapitulatif_ticket_Adulte");
      $lbl_ticket_persoa = $("#recapitulatif_ticket_PersoA");
      $lbl_ticket_recap = $("#recapitulatif_total");
      $lbl_username = $("#lbl_username");
      $lbl_haut = $("#lbl_haut");
      $lbl_bas = $("#lbl_bas");
      $lbl_footer_username = $("#footer_lbl_username");
      $lbl_footer_haut = $("#footer_lbl_haut");
      $lbl_footer_bas = $("#footer_lbl_bas");
      /*MODIFICATION*/
      $mdf_logo = $("#img_logo");
      $mdf_link_home = $("#modification_link");
      $mdf_footer_link_home = $("#footer_home_link");
      $mdf_account_link = $("#account_option");
      
      /*VAR*/
      var arrQte = [];
      var emptyArr = [];
      var co;
      emptyArr.push("alert-info");
      
      /*EVENT*/
      $btn_ticket_enfant_moins.click(modifyQte);
      $btn_ticket_enfant_plus.click(modifyQte);
      $btn_ticket_adulte_moins.click(modifyQte);
      $btn_ticket_adulte_plus.click(modifyQte);
      $btn_ticket_persoa_moins.click(modifyQte);
      $btn_ticket_persoa_plus.click(modifyQte);

      $btn_recapitulatif_annuler.click(setDefaultValues);
      $btn_ticket_persoa_confirmer.click(reservationBillet);

      /*CODE*/
      isConnected();
      setDefaultValues();  
      isAdmin();
      /*FUNCTION*/
      function setDefaultValues(){
        for (var cpt = 0; cpt < 3; cpt++) {
          arrQte[cpt] = 0;
        }
        setAffichage();
      }
      
      function modifyQte(){
        affiliation($(this));
        setAffichage();
      }

      function setAffichage(){
        $lbl_ticket_enfant.html(arrQte[0]);
        $lbl_ticket_adulte.html(arrQte[1]);
        $lbl_ticket_persoa.html(arrQte[2]);
        $lbl_ticket_recap.html((arrQte[1]*15)+(arrQte[2]*7.5));
      }

      function affiliation(obj){
        switch(obj.attr("id")) {
          case "btn_ticket_enfant_moins":
          changeQt(0,0);
          break;
          case "btn_ticket_enfant_plus":
          changeQt(0,1);
          break;
          case "btn_ticket_adulte_moins":
          changeQt(1,0);
          break;
          case "btn_ticket_adulte_plus":
          changeQt(1,1);
          break;
          case "btn_ticket_persoa_moins":
          changeQt(2,0);
          break;
          case "btn_ticket_persoa_plus":
          changeQt(2,1);
          break;
        } 
      }

     function changeQt(position,event){
        if (event == 0) {
          if(arrQte[position]-1 < 0){
            console.log("erreur");
          }else{
            arrQte[position] -= 1;
          }
        }else{
          arrQte[position] += 1;
        }
     }
     
     function isConnected(){
    	 $.getJSON('UtilisateurJson', {
     		"service" : "5"
       	  })
       	    .done(function(data) {
       	    	console.log("Statut : "+data.statut);
       	    	console.log("Msg : "+data.msg);
       	    	console.log("Co : "+data.co);
       	    	if(data.co){
       	    		changeHtml();
       	    		co = true;
       	    	}
       	});
    	 
     }
     
     function changeHtml(){
    		 $.getJSON('UtilisateurJson', {
         		"service" : "1"
           	  })
           	    .done(function(data) {
           	      $lbl_username.html(data.username_User);
           	      $lbl_footer_username.html(data.username_User);
           	      if(data.statut != "SUCCESS"){
           	    	setMessageAndState($alert_recapitulatif,$alert_recapitulatif_message,getAlert(3),data.msg);  
           	      }
           	});
    		$lbl_haut.attr({
    	   		  "type": "",
    	   		  "data-toggle": "",
    	   		  "data-target": "",
    	   		  "href":"AccountPage"
    			});
    	    $lbl_haut.html("Mon compte");
    	    $lbl_bas.attr({
    	          "type": "",
    	       	  "data-toggle": "",
    	       	  "data-target": "",
    	   		  "href":"Logout"
    	    	});
    	    $lbl_footer_haut.attr({
  	   		  "type": "",
	   		  "data-toggle": "",
	   		  "data-target": "",
	   		  "href":"AccountPage"
			});
    	    $lbl_footer_haut.html("Mon compte");
    	    $lbl_footer_bas.attr({
  	          "type": "",
	       	  "data-toggle": "",
	       	  "data-target": "",
	   		  "href":"Logout"
	    	});
    	    $lbl_footer_bas.html("Se déconnecter");
    	    $lbl_bas.html("Se déconnecter");
    	    $mdf_logo.attr("href","HomeUser");
    	    $mdf_link_home.attr("href","HomeUser");
    	    $mdf_footer_link_home.attr("href","HomeUser");
    	 
     }
     
     function reservationBillet(){
    	 //VERIFICATION SI USER CO OU PAS
    	 if(co){
    		 buyTicket();
    	 }else{
    		 setMessageAndState($alert_recapitulatif,$alert_recapitulatif_message,getAlert(3),"vous devez être connecté pour acheter des tickets");
    	 }
     }
     
     function setMessageAndState(objParent,objFils,state,message){
         objParent.toggleClass(emptyArr[0]+" "+state);
         emptyArr.pop();
         emptyArr.push(state);
         objFils.empty();
         objFils.html(message);
     }

     function getAlert(state){
     var str ="";
     switch(state) {
         case 0:
         str = "alert-success";
         break;
         case 1:
         str = "alert-info";
         break;
         case 2:
         str = "alert-warning";
         break;
         case 3:
         str = "alert-danger";
         break;
         } 
     return str;
     }
     
     function buyTicket(){
    	 if((arrQte[0]+arrQte[1]+arrQte[2]) != 0){
    		 $.getJSON('Ticket', {
          		"ticket_enfant" : arrQte[0],
          		"ticket_adulte" : arrQte[1],
          		"ticket_vieux" : arrQte[2]
            	  })
            	    .done(function(data) {
            	      console.log("Statut : "+data.statut);
              	      console.log("Msg : "+data.msg);
              	      setDefaultValues();
            	      if(data.statut != "SUCCESS"){
            	    	setMessageAndState($alert_recapitulatif,$alert_recapitulatif_message,getAlert(3),data.msg);  
            	      }else{
            	    	  setMessageAndState($alert_recapitulatif,$alert_recapitulatif_message,getAlert(0),"Ticket(s) reservé(s)");
            	    	  setTimeout(resetAlert,3000);
            	      }
            	});
    	 }else{
    		 setMessageAndState($alert_recapitulatif,$alert_recapitulatif_message,getAlert(3),"Vous devez au minimum avoir choisit un ticket avant de confirmer");
    	 }
    	 
     }
     
     function resetAlert(){
      	setMessageAndState($alert_recapitulatif,$alert_recapitulatif_message,getAlert(1),"Vous pouvez consulter l'état de votre commande ici");
      }
     function isAdmin(){//7
    	 $.getJSON('UtilisateurJson', {
      		"service" : "7"
        	  })
        	    .done(function(data) {
        	      console.log(data.id_User);
        	      if(data.id_User == 4){
        	    	  adminAccess();
        	      }
        	      if(data.statut != "SUCCESS"){
        	    	console.log(data.msg);  
        	      }
        	});
     }
     
     function adminAccess(){
    	$baliseAdmin = $("<a>").attr({"href":"AdminPage","class":"dropdown-item"});
    	$baliseAdmin.html("Admin");
    	
    	$baliseAdmin.appendTo($mdf_account_link);
     }
 
});