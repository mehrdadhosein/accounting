		function showMoeenList(moeen,tafsili){
			//debugger;
			//console.log('showMoeenList');
			var moeenId = $$('#hesabMoeen_id').val();
			
			if(!(moeenId == null || moeenId == ''))
				return;
			clearAutocomplete(moeen);
			var tafsiliId = $$('#hesabTafsili_id').val();
			var tafsiliDesc = $$('#hesabTafsili_desc').val();
			
			$$('#tafsiliMoeenList').html('');
			var moeenMap = tafsiliMoeenMap[tafsiliId];
			var index =0;
			
			var tbl = $$('#tafsiliMoeenTable');
			tbl.html('');
			var tHead = $$('<thead>');
			var tHeadRow = $$('<tr>');
			tHeadCellKol = $$('<th style="width: 300px;">').append("حساب کل").addClass("ui-state-default");
			tHeadCellMoeen = $$('<th style="width: 300px;">').append("حساب معین").addClass("ui-state-default");
			tHeadRow.append(tHeadCellKol);
			tHeadRow.append(tHeadCellMoeen);
			tHead.append(tHeadRow);
			tbl.append(tHead); 
			
			for(index =0 ; index<moeenMap.length; index++){
				//alert(moeenMap[index].label);
				var id = moeenMap[index].value;
				var label = moeenMap[index].label;
				var tRow = $$('<tr>');
				
				kolLabel = moeenKolMap[id].label;
				var tStrong1 = $$('<strong>').html(kolLabel);
				var tP1 = $$('<p>').append(tStrong1);
				tCellKol = $$('<td style="width: 300px;">').append(tP1).addClass("ui-widget-content");
				
				var outputLink = '<a onclick="onSelectMoeenOnDialogue('+id+',\''+label+'\','+tafsiliId+',\''+tafsiliDesc+'\')" style="height:100px;" class="">'+moeenMap[index].label+'</a>';
				var tStrong2 = $$('<strong>').html(outputLink);
				var tP2 = $$('<p>').append(tStrong2);
				tCellMoeen = $$('<td style="width: 300px;">').append(tP2).addClass("ui-widget-content").css({ cursor: "pointer"});
				
				tRow.append(tCellKol);
				tRow.append(tCellMoeen);
				
				//var outputLink = $$('<a onclick="onSelectMoeenOnDialogue('+id+',\''+label+'\','+tafsiliId+',\''+tafsiliDesc+'\')" style="height:100px;" class="">'+moeenMap[index].label+'</a>');
				//$$('#tafsiliMoeenTable').append(outputLink);
				tbl.append(tRow);

			}
			$$('#tafsiliMoeenDIV').dialog({width: '600px'});
			//$$('#tafsiliMoeenDIV').modal('toggle');
			//$$('#tafsiliMoeenDIV').modal({backdrop: 'static', keyboard: true});
			//alert("unitMap : "+unitMap.length);
			//var filtered = moeenMap;
			//alert(inspect(filtered,10,10));

		}

		function onSelectMoeenOnDialogue(id, label, tafsiliId, tafsiliDesc){
			$$('#hesabMoeen_id').val(id);
			$$('#hesabMoeen_desc').val(label);
			fillDest('hesabMoeen','hesabTafsili');
			$$('#hesabTafsili_id').val(tafsiliId);
			$$('#hesabTafsili_desc').val(tafsiliDesc);	
			$$('#tafsiliMoeenDIV').dialog('close')
			//$$('#tafsiliMoeenDIV').modal('toggle');
		}