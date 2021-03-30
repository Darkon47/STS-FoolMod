package eatyourbeets.cards.animator.series.LogHorizon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.RainbowCardEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Minori extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Minori.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None);

    public Minori()
    {
        super(DATA);

        Initialize(0, 6, 50);
        SetUpgrade(0, 1, 25);

        SetSpellcaster();
        SetCooldown(4, 0, this::OnCooldownCompleted);
        SetSynergy(Synergies.LogHorizon);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);

        cooldown.ProgressCooldownAndTrigger(m);

        if (HasSynergy() && CombatStats.TryActivateSemiLimited(cardID))
        {
            ShuffleToTopOfDeck();
        }
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        ShuffleToTopOfDeck();
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        GameActions.Bottom.Callback(c ->
        {
            GameActions.Bottom.GainBlock((int)(player.currentBlock * (1 + (magicNumber/100f))));
            GameActions.Bottom.VFX(new RainbowCardEffect());
        });
    }

    private void ShuffleToTopOfDeck()
    {
        GameUtilities.Flash(this, false);
        GameActions.Last.MoveCard(this, player.drawPile);
    }
}