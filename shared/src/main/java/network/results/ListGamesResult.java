package network.results;

import network.dataModels.GameData;

import java.util.ArrayList;
import java.util.Collection;

public record ListGamesResult(Collection<GameData> games) {
}
