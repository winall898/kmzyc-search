
//��ʾѡ�����µ���������
function displayThreeSelect(){
    /*
        ��ʾʱ��������
    */
    var selectTime = '<select name="selectTime" id="selectTime" class="timenp1" onchange="changeTime(this);">';        //ʱ������������
    
    var tempDate = new Date();          //��ʱʱ��
	var nowDate = new Date();			//����ʱ��
    for(var i=0;i<7;i++){
        //��i���ʱ��
        tempDate = new Date(nowDate.getTime()+i*86400000); 
        var dateStr = tempDate.format("yyyy-M-d");
        //��װ�������е�ʱ��
        if(dateStr==gplayTime){
            selectTime += '<option selected value="'+dateStr+'">'+dateStr+'</option>';
        }else{
            selectTime += '<option value="'+dateStr+'">'+dateStr+'</option>';
        }
    }
	selectTime += '</select>';
    //��ʾ������
    document.getElementById("gameTime").innerHTML = selectTime;

    /*
        ��ʾ����������
    */

    //��������������
    var selectPlay = '<select name="selectPlay" id="selectPlay" class="leaguenp21">';

    var playArray = selectPlayStr.split("^");
    var gameInitTemp = new Array();
    for(var i=0;i<playArray.length;i++){
        /*
            cellPlay�ĸ�ʽ���£� 
            0��gplayId
            1��gameType
            2��gameId
            3��gplayTime ��yyy-MM-dd��
            4��hostNameCn
            5��visitNameCn
            6��gliveId
            7��gplayTime��yyyy-MM-dd HH:mm��
        */
        cellPlay = playArray[i].split(",");
        if(cellPlay[3]==gplayTime){     //����ǵ�ǰ���ڵı���
            if(gameInitTemp.length==0){     //����Ϊ�գ���������ID
                gameInitTemp.push(cellPlay[2]);
            }else{
                if(!isExist(cellPlay[2],gameInitTemp)){      //��������д��ڴ�����
                    gameInitTemp.push(cellPlay[2]);
                }
            }
			
            if(cellPlay[2]==gameId && cellPlay[6]==gliveId){        //�����ǵ�ǰ��������ǰ������ѡ��
				//����value�������ӺͿͶ����ּ�����ʱ��Ϊָ����������
                selectPlay += '<option selected value="'+cellPlay[6]+'_'+cellPlay[4]+'_'+cellPlay[5]+'_'+cellPlay[7]+'">'+cellPlay[4]+'VS'+cellPlay[5]+'</option>';
            }else if(cellPlay[2]==gameId){          
                selectPlay += '<option value="'+cellPlay[6]+'_'+cellPlay[4]+"_"+cellPlay[5]+'_'+cellPlay[7]+'">'+cellPlay[4]+'VS'+cellPlay[5]+'</option>';
            }
        }
    }
    selectPlay += '</select>';
    document.getElementById("play").innerHTML = selectPlay;

    /*
        ��ʾ����������
    */
    var gameArray = selectGameStr.split("^");
    //��������������
    var selectGame = "<select name='selectGame' id='selectGame' class='leaguenp1' onchange='changeGame(this)'>";
    for(var i=0;i<gameArray.length;i++){
        /*
            cellGame�ĸ�ʽ���£�
            0��gamType
            1��gameId
            2��gameNameCn
        */
        var cellGame = gameArray[i].split(",");
        for(var j=0;j<gameInitTemp.length;j++){     //ѭ����������
            if(cellGame[1]==gameInitTemp[j]){       //�����ȣ�����ʾ
                if(gameId==cellGame[1]){            //���ڵ�ǰ������ID����ѡ��״̬
                    selectGame += '<option selected value="'+cellGame[1]+'">'+cellGame[2]+'</option>';
                    continue;
                }else{
                    selectGame += '<option value="'+cellGame[1]+'">'+cellGame[2]+'</option>';
                    continue;
                }
            }
        }
    }
    selectGame += '</select>';
    document.getElementById("game").innerHTML = selectGame;
}


//ѡ������
function changeTime(thisObj){

    /*
        ѡ��ʱ���ó��Ķ����б� (ѡ�����ڵ����б���)
    */
    //ѡ�е�����
    var currTime = thisObj.options[thisObj.options.selectedIndex].value;

    var tempGamIdArray = new Array();    //��ʱ����ID
    var playData = selectPlayStr.split("^");
    for(var i=0;i<playData.length;i++){
         /*
            playCell�ĸ�ʽ���£� 
            0��gplayId
            1��gameType
            2��gameId
            3��gplayTime ��yyy-MM-dd��
            4��hostNameCn
            5��visitNameCn
            6��gliveId
            7��gplayTime��yyyy-MM-dd HH:mm��
        */
        var playCell = playData[i].split(",");
        if(currTime==playCell[3]){      //�����ѡ��ʱ��
            if(tempGamIdArray.length==0){   //�����һ������ID
                tempGamIdArray.push(playCell[2]);
            }else{
                if(!isExist(playCell[2],tempGamIdArray)){   //�����ڴ�gameId��������,����
                    tempGamIdArray.push(playCell[2]);
                }
            }
        }
    }
    /*
        ����ʱ�����б��������б�,����ʾ
    */
    var gameData = selectGameStr.split("^");
    var gameHtml = "<select name='selectGame' id='selectGame' class='leaguenp1' onchange='changeGame(this)'>";
    var flag = true;
    var currGame;       //Ĭ�ϵ�����
    for(var i=0;i<gameData.length;i++){     //ѭ������
         /*
            cellGame�ĸ�ʽ���£�
            0��gamType
            1��gameId
            2��gameNameCn
        */
        var gameCell = gameData[i].split(",");
        for(var j=0;j<tempGamIdArray.length;j++){   //ѭ����ʱ����ID
            if(gameCell[1]==tempGamIdArray[j]){     //������
                if(flag){                           //��һ������ΪĬ������
                    currGame = gameCell[1];
                    flag = false;
                    gameHtml += '<option selected value="'+gameCell[1]+'">'+gameCell[2]+'</option>';
                }else{
                    gameHtml += '<option value="'+gameCell[1]+'">'+gameCell[2]+'</option>';
                }
                continue;
            }
        }
    }
    gameHtml += '</select>';
    document.getElementById("game").innerHTML = gameHtml;

    /*
        ��ѡ���ʱ�估Ĭ�ϵ�������ʾ�����б�(currTime��currGame)
    */
    var playHtml = '<select name="selectPlay" id="selectPlay" class="leaguenp21">';
    for(var i=0;i<playData.length;i++){
        /*
            playCell�ĸ�ʽ���£� 
            0��gplayId
            1��gameType
            2��gameId
            3��gplayTime ��yyy-MM-dd��
            4��hostNameCn
            5��visitNameCn
            6��gliveId
            7��gplayTime��yyyy-MM-dd HH:mm��
        */
        var playCell = playData[i].split(",");
        if(playCell[3]==currTime && playCell[2]==currGame){
			//����value�������ӺͿͶ����ּ�����ʱ��Ϊָ����������
            playHtml += '<option value="'+playCell[6]+'_'+playCell[4]+'_'+playCell[5]+'_'+playCell[7]+'">'+playCell[4]+'VS'+playCell[5]+'</option>';
        }
    }
    playHtml += '</select>';
    document.getElementById("play").innerHTML = playHtml;
}

//ѡ������
function changeGame(thisObj){
    //ѡ�������ID
    var currGame = thisObj.options[thisObj.options.selectedIndex].value;
    //��ǰ��ʱ��
	var index = document.getElementById('selectTime').options.selectedIndex;
	var currTime = document.getElementById('selectTime').options[index].value;	
    var playHtml = '<select name="selectPlay" id="selectPlay" class="leaguenp21">';
    var playData = selectPlayStr.split("^");
    for(var i=0;i<playData.length;i++){     //ѭ�����ж���
        /*
            playCell�ĸ�ʽ���£� 
            0��gplayId
            1��gameType
            2��gameId
            3��gplayTime ��yyy-MM-dd��
            4��hostNameCn
            5��visitNameCn
            6��gliveId
            7��gplayTime��yyyy-MM-dd HH:mm��
        */
        var playCell = playData[i].split(",");
        if(playCell[2]==currGame && playCell[3]==currTime){     //�������ʱ��������ID��ͬ������ʾ
			//����value�������ӺͿͶ����ּ�����ʱ��Ϊָ����������
            playHtml += '<option value="'+playCell[6]+'_'+playCell[4]+'_'+playCell[5]+'_'+playCell[7]+'">'+playCell[4]+'VS'+playCell[5]+'</option>';
        }
    }
    playHtml += '</select>';
    document.getElementById("play").innerHTML = playHtml;
    
}

//�ж�����ID�Ƿ�����ڴ��ַ�����
function isExist(gameId,array){
    for(var i=0;i<array.length;i++){
        if(array[i]==gameId){
            return true;
        }
    }
    return false;
}

