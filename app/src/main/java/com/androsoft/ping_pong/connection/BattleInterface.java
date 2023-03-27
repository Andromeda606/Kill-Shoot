package com.androsoft.ping_pong.connection;

import com.androsoft.ping_pong.constant.Character;

public interface BattleInterface {
    abstract class OnBattleInit {
        // Başlangıç istek seçimi
        public void onRequest(String ipAddress) // find
        {

        }

        // Savaş teklifine karşı taraftan yanıt alındı
        public void catchProcess(String ipAddress, Boolean status) // accept - reject
        {

        }

        // Kullanıcı seçim sistemi
        // chr: Type
        public void characterSelected(String ipAddress, int characterType){

        }
    }

    abstract class OnGameProcess {
        public void shoot() {

        }
        public void setup(Character.Type characterType) {

        }

        public void paired() {

        }

        public void xyStatus(float x, float y) {

        }

        public void pairedSuccessfull(){

        }
    }

    void message(String data, String ipAddress);

}
