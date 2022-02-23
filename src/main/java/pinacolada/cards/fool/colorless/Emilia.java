package pinacolada.cards.fool.colorless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import pinacolada.actions.orbs.EvokeOrb;
import pinacolada.cards.base.*;
import pinacolada.cards.fool.FoolCard;
import pinacolada.orbs.PCLOrbHelper;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class Emilia extends FoolCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final PCLCardData DATA = Register(Emilia.class)
            .SetSkill(2, CardRarity.RARE, PCLCardTarget.None)
            .SetColorless()
            .SetMultiformData(2, false)
            .SetSeries(CardSeries.ReZero);

    public Emilia()
    {
        super(DATA);

        Initialize(0, 1, 2, 0);

        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Light(1);

        SetEvokeOrbCount(magicNumber);
        SetExhaust(true);
    }

    @Override
    protected String GetRawDescription(Object... args)
    {
        return super.GetRawDescription(cardData.Strings.EXTENDED_DESCRIPTION[auxiliaryData.form]);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (timesUpgraded > 0) {
            if (form == 1) {
                SetUpgrade(0, 0, 0, 2);
            }
            else {
                SetUpgrade(0, 3, 0, 0);
                AddScaling(PCLAffinity.Blue, 2);
            }
        }
        return super.SetForm(form, timesUpgraded);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.EvokeOrb(player.filledOrbCount(), EvokeOrb.Mode.Sequential).AddFocus(secondaryValue).AddCallback(() -> {
            PCLActions.Bottom.ChannelOrbs(PCLOrbHelper.Frost, magicNumber);
        });
        PCLCombatStats.onStartOfTurnPostDraw.Subscribe((Emilia) makeStatEquivalentCopy());
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        PCLGameEffects.Queue.ShowCardBriefly(this);
        PCLActions.Bottom.GainOrbSlots(PCLGameUtilities.GetUniqueOrbsCount());
        PCLCombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
    }
}