package network.results;

import network.data_models.GameData;

import java.util.Collection;

public record ListGamesResult(Collection<GameData> games) {
}
