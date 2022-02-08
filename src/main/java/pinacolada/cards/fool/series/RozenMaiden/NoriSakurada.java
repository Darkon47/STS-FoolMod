package pinacolada.cards.fool.series.RozenMaiden;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.subscribers.OnEndOfTurnFirstSubscriber;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.fool.FoolCard;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.common.DelayedDamagePower;
import pinacolada.utilities.PCLActions;

public class NoriSakurada extends FoolCard implements OnEndOfTurnFirstSubscriber
{
    public static final PCLCardData DATA =
            Register(NoriSakurada.class)
                    .SetSkill(0, CardRarity.COMMON, PCLCardTarget.None).SetSeriesFromClassPackage();

    public NoriSakurada() {
        super(DATA);

        Initialize(0, 0, 1, 2);
        SetUpgrade(0, 0, 0, 0);
        SetAffinity_Light(1, 0, 0);

        SetAffinityRequirement(PCLAffinity.Light, 3);

        SetRetainOnce(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {

        PCLActions.Top.FetchFromPile(name, 1, player.discardPile)
                .SetOptions(true, false);
        PCLActions.Bottom.DiscardFromHand(name, 1, false);

        if (CheckAffinity(PCLAffinity.Light)) {
            PCLCombatStats.onEndOfTurn.SubscribeOnce(this);
        }
    }

    @Override
    public void OnEndOfTurnFirst(boolean isPlayer) {
        PCLActions.Top.Reload(NoriSakurada.DATA.Strings.NAME, cards -> {
            if (cards.size() > 0) {
                PCLActions.Top.ReducePower(player, player, DelayedDamagePower.POWER_ID, cards.size() * secondaryValue);
            }
        });
    }
}
