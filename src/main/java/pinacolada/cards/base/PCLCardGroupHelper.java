package pinacolada.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.options.InputSettingsScreen;
import org.apache.commons.lang3.StringUtils;
import pinacolada.resources.PGR;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PCLCardGroupHelper
{
    public static final Map<CardGroup.CardGroupType, PCLCardGroupHelper> ALL = new HashMap<>();

    public static final PCLCardGroupHelper DiscardPile = new PCLCardGroupHelper(CardGroup.CardGroupType.DISCARD_PILE, InputSettingsScreen.TEXT[12], PGR.PCL.Strings.Actions.DiscardPile);
    public static final PCLCardGroupHelper DrawPile = new PCLCardGroupHelper(CardGroup.CardGroupType.DRAW_PILE, InputSettingsScreen.TEXT[13], PGR.PCL.Strings.Actions.DrawPile);
    public static final PCLCardGroupHelper ExhaustPile = new PCLCardGroupHelper(CardGroup.CardGroupType.EXHAUST_PILE, InputSettingsScreen.TEXT[14], PGR.PCL.Strings.Actions.ExhaustPile);
    public static final PCLCardGroupHelper Hand = new PCLCardGroupHelper(CardGroup.CardGroupType.HAND, PGR.PCL.Strings.Combat.Hand, PGR.PCL.Strings.Actions.Hand);

    public final CardGroup.CardGroupType Pile;
    public final String Name;
    public final String CardString;

    public static PCLCardGroupHelper Get(String name) {
        return ALL.get(CardGroup.CardGroupType.valueOf(name));
    }

    public static PCLCardGroupHelper Get(AbstractCard.CardTags tag) {
        return ALL.get(tag);
    }

    public static List<PCLCardGroupHelper> GetAll() {
        return ALL.values().stream().sorted((a, b) -> StringUtils.compare(a.Name, b.Name)).collect(Collectors.toList());
    }

    public PCLCardGroupHelper(CardGroup.CardGroupType pile, String name, String cardString)
    {
        this.Pile = pile;
        this.Name = name;
        this.CardString = cardString;

        ALL.putIfAbsent(Pile, this);
    }

    public final CardGroup GetCardGroup() {
        if (AbstractDungeon.player != null) {
            switch (Pile) {
                case HAND:
                    return AbstractDungeon.player.hand;
                case DRAW_PILE:
                    return AbstractDungeon.player.drawPile;
                case DISCARD_PILE:
                    return AbstractDungeon.player.discardPile;
                case EXHAUST_PILE:
                    return AbstractDungeon.player.exhaustPile;
                case MASTER_DECK:
                    return AbstractDungeon.player.masterDeck;
            }
        }
        return null;
    }
}
