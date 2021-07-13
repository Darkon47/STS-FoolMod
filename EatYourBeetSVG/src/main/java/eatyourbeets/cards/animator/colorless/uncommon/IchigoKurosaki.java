package eatyourbeets.cards.animator.colorless.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.IchigoBankai;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.powers.affinity.ForcePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class IchigoKurosaki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(IchigoKurosaki.class)
            .SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.Bleach);
    static
    {
        DATA.AddPreview(new IchigoBankai(), false);
    }

    public IchigoKurosaki()
    {
        super(DATA);

        Initialize(0, 0, 0, 5);

        SetAffinity_Red(1);
        SetAffinity_Green(1);

        SetExhaust(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.Callback(() ->
        {
            if (GameUtilities.GetPowerAmount(ForcePower.POWER_ID) >= secondaryValue)
            {
                GameActions.Bottom.MakeCardInDrawPile(new IchigoBankai());
            }
        });
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainForce(1, true);
        GameActions.Bottom.GainAgility(1, true);

        if (upgraded)
        {
            GameActions.Bottom.Draw(1);
        }
    }
}