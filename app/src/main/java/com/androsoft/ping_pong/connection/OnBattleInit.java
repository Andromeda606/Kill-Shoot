package com.androsoft.ping_pong.connection;

import com.androsoft.ping_pong.constant.Character;

public abstract class OnBattleInit {
    // Başlangıç istek seçimi
    public abstract void onRequest(String ipAddress); // find
    // Savaş teklifine karşı taraftan yanıt alındı
    public abstract void catchProcess(String ipAddress, Boolean status); // accept - reject

    // Kullanıcı seçim sistemi
    // chr: Type
    void characterSelected(String ipAddress, Character.Type characterType) throws EndConnection{

    }
}
