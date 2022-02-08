package pinacolada.cards.fool.curse;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.*;
import pinacolada.cards.fool.FoolCard;
import pinacolada.cards.fool.special.UndulatingLacuna;
import pinacolada.utilities.PCLActions;

public class Curse_Delusion extends FoolCard
{
    public static final PCLCardData DATA = Register(Curse_Delusion.class)
            .SetCurse(-2, PCLCardTarget.None, false, true).SetSeries(CardSeries.GenshinImpact)
            .PostInitialize(data -> data.AddPreview(new UndulatingLacuna(), true));

    public Curse_Delusion()
    {
        super(DATA);
        SetAffinity_Dark(1);
        SetUnplayable(true);

        SetAffinityRequirement(PCLAffinity.General, 16);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (this.dontTriggerOnUseCard) {
            PCLActions.Bottom.ModifyTag(player.drawPile, 1, AUTOPLAY, true);
        }
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (CheckAffinity(PCLAffinity.General) && CombatStats.TryActivateLimited(cardID)) {
            AbstractCard c = new UndulatingLacuna();
            c.applyPowers();
            PCLActions.Bottom.PlayCopy(c, null);
        }
    }

}