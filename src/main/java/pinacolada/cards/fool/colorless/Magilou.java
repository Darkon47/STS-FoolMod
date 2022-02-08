package pinacolada.cards.fool.colorless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.powers.CombatStats;
import pinacolada.actions.orbs.ShuffleOrbs;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.fool.FoolCard;
import pinacolada.cards.fool.special.Magilou_Bienfu;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Magilou extends FoolCard
{
    public static final PCLCardData DATA = Register(Magilou.class)
            .SetSkill(1, CardRarity.UNCOMMON, PCLCardTarget.None)
            .SetMaxCopies(1)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.TalesOfBerseria)
            .PostInitialize(data -> data.AddPreview(new Magilou_Bienfu(), false));

    public Magilou()
    {
        super(DATA);

        Initialize(0, 0, 0);

        SetAffinity_Blue(1);

        SetExhaust(true);
    }

    @Override
    public void triggerWhenDrawn()
    {
        if (CombatStats.TryActivateLimited(cardID))
        {
            PCLActions.Top.Discard(this, player.hand).ShowEffect(true, true)
            .AddCallback(() -> PCLActions.Top.MakeCardInHand(new Magilou_Bienfu()))
            .SetDuration(0.5f, true);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        AbstractOrb firstOrb = PCLGameUtilities.GetFirstOrb(null);
        AbstractOrb newOrb = firstOrb != null ? firstOrb.makeCopy() : new Lightning();
        PCLActions.Bottom.ChannelOrb(newOrb);
        PCLActions.Bottom.Callback(new ShuffleOrbs(1)).AddCallback(() -> {
            if (upgraded) {
                PCLActions.Bottom.TriggerOrbPassive(player.orbs.size(), false, true);
            }
        });
    }
}