package network.results;

import network.dataModels.GameData;

import java.util.ArrayList;

public record ListGamesResult(ArrayList<GameData> games) {
}
