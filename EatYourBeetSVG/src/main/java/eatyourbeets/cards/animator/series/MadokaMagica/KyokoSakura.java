package eatyourbeets.cards.animator.series.MadokaMagica;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class KyokoSakura extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KyokoSakura.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Piercing, EYBCardTarget.Random);

    public KyokoSakura()
    {
        super(DATA);

        Initialize(9, 0, 1);
        SetUpgrade(0, 0, 1);
        SetScaling(0, 0, 1);

        SetSynergy(Synergies.MadokaMagica);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
        GameActions.Bottom.Draw(magicNumber).AddCallback(cards ->
        {
            for (AbstractCard card : cards)
            {
                if (GameUtilities.IsCurseOrStatus(card))
                {
                    GameActions.Bottom.ChannelOrb(new Fire(), true);
                    return;
                }
            }
        });

        GameActions.Bottom.SelectFromHand(name, magicNumber, false)
        .SetMessage(GR.Common.Strings.HandSelection.MoveToDrawPile)
        .AddCallback(cards ->
        {
            for (AbstractCard card : cards)
            {
                GameActions.Top.MoveCard(card, player.hand, player.drawPile)
                .AddCallback(c -> JUtils.ChangeIndex(c, player.drawPile.group, player.drawPile.size()));
            }
        });
    }
}
