$(document).ready(function(){

      /*AFFICHAGE VALEUR*/
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
      var co;

      /*CODE*/
      changeHtml();
      isAdmin();

     function changeHtml(){
    		 $.getJSON('UtilisateurJson', {
         		"service" : "1"
           	  })
           	    .done(function(data) {
           	      $lbl_username.html(data.username_User);
           	      $lbl_footer_username.html(data.username_User);
           	      if(data.statut != "SUCCESS"){
           	    	console.log(data.msg);  
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
    	$baliseAdmin = $("<a>").attr({"href":"Admin","class":"dropdown-item"});
    	$baliseAdmin.html("Admin");
    	
    	$baliseAdmin.appendTo($mdf_account_link);
     }
 
});