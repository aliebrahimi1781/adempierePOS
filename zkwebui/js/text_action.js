
function Text_action()
{

	
	this.clearAlll = clearAlll;
	

	function clearAlll(calcTextId, key)
	{
		try
		{
			var entry = key;
        	var calcText = document.getElementById(calcTextId);
			var oldValue = calcText.value+key;
			var res = "";
			if(calcText.selectionStart != calcText.selectionEnd){
	            if (window.getSelection) {  // all browsers, except IE before version 9
	                
	                    var selRange = window.getSelection ();

	                    res = oldValue.replace(selRange.toString(), "");
	                    calcText.value = res;
	                    
	            }
	            else {
	                if (document.selection.createRange) { // Internet Explorer
	                    var range = document.selection.createRange ();
	                    res = oldValue.replace(range.text, "");
	                    calcText.value = res;
	                }
	            }
	            if (res !== "") {
	                alert (res);
	            }
			}else {
			if ( entry != null && entry!="" )
			{	

				if(entry=="-"){
//					int last = txtCalc.getText().length();
//					txtCalc.setText(txtCalc.getText().substring(0, last-1));
					
				}
				else if ( key != null )
					calcText.value= calcText.value + entry ;
				
				else if ( entry=="." )
					{
					calcText.value=calcText.value + entry;
					}
					if ( entry=="," )
					{
						calcText.value=calcText.value + entry;
					}
					else if ( entry=="C" )
					{
						calcText.value="0";
					}
					else {
					try
					{
						var number = parseInt(entry);		// test if number
						if ( number >= 0 && number <= 9 )
						{
							calcText.value=calcText.value+number;
						}
						// greater than 9, add to existing
						else 
						{
//							Boolean current = txtCalc.getValue().contains(".");
//							if ( current==true )
//							{
//								txtCalc.value=number+parseDouble(tail)+"";
//							}
//							else
//							{
//								txtCalc.value=""+parseInt(tail)+number;
//							}
														
						}
	
	
					}
					catch (err)
					{
						// ignore non-numbers
					}
					}
					
				}
			}
				
		        
		}
		catch (err)
		{
		}
	}
	

	
}

var text_action = new Text_action();